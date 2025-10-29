package com.ecommerce.order.serviceimpl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ecommerce.order.client.CustomerClient;
import com.ecommerce.order.client.InventoryClient;
import com.ecommerce.order.client.NotificationClient;
import com.ecommerce.order.client.PaymentClient;
import com.ecommerce.order.client.ShippmentClient;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.exception.OrderException;
import com.ecommerce.order.pojo.Customer;
import com.ecommerce.order.pojo.Notification;
import com.ecommerce.order.pojo.Payment;
import com.ecommerce.order.pojo.Shipment;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Order Service class
 * 
 * @author Suresh Injeti
 *
 */
@Service
//@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Value("${app.rest.url}")
	private String apiHost;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private InventoryClient inventoryClient;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private CustomerClient customerClient;

	@Autowired
	private ShippmentClient shipmentClient;

	@Autowired
	private PaymentClient paymentClient;

	@Autowired
	private OrderItemRepository orderItemRepository;

	private final MeterRegistry meterRegistry;

	private Timer reserveInventoryTimer;
	private Counter ordersPlaced;
	private Counter orderFailed;
	private Counter paymentsFailed;
	private Counter stockouts;

	RestClient restClient = RestClient.builder().baseUrl(apiHost).build();

	public OrderServiceImpl(MeterRegistry registry) {
		this.meterRegistry = registry;
		this.ordersPlaced = Counter.builder("orders_placed_total").tag("service", "order-service").register(registry);
		this.orderFailed = Counter.builder("orders_placed_failed").tag("service", "order-service").register(registry);
		this.paymentsFailed = Counter.builder("payments_failed_total").tag("service", "order-service")
				.register(registry);
		this.stockouts = Counter.builder("stockouts_total").tag("service", "order-service").register(registry);

		this.reserveInventoryTimer = Timer.builder("inventory_reserve_latency_ms")
				.description("Latency for inventory reservation").tag("service", "order-service")
				.publishPercentiles(0.5, 0.95).register(registry);
	}

	@Override
	public List<Order> getAllOrders() throws OrderException {
		List<Order> orderList = orderRepository.findAll();
		if (orderList == null || orderList.isEmpty())
			throw new OrderException("No order available");
		else
			return orderList;
	}

	@Override
	public Order getOrderById(int orderId) throws OrderException {
		Optional<Order> order = orderRepository.findById(orderId);
		if (order.isEmpty())
			throw new OrderException("Order matach found with '" + orderId + "' ");
		else
			return order.get();
	}

	@Transactional
	@Override
	public void createOrder(Order order) throws OrderException {
		Shipment shipment = new Shipment();
		Payment payment = new Payment();

		try {
			int index = orderRepository.getOrderIncrement();
			int itemIndex = orderItemRepository.getOrderItemIncrement();
			index = index + 1;
			order.setOrderId(index);
			double totalPrice = 0.00;
			String idempotencyKey = UUID.randomUUID().toString();
			// (Σ unit_price × qty + 5% tax + shipping)
			for (OrderItem item : order.getItems()) {

				boolean inStock = inventoryClient.isInStock(item.getProductId(), item.getQuantity());
				if (!inStock) {
					stockouts.increment();
					meterRegistry.counter("stockouts_total").increment();
					throw new OrderException("Product is not in stock. Please try again");
				}
				itemIndex = itemIndex + 1;
				item.setOrderItemId(itemIndex);
				item.setOrder_id(index);
				totalPrice = totalPrice + (item.getUnitPrice() * item.getQuantity());

				// latency
				reserveInventoryTimer.record(() -> {
					// Reserve product in inventory
					//String idempotencyKey = UUID.randomUUID().toString();
					inventoryClient.reserveProducct(item.getProductId(), item.getQuantity(), idempotencyKey);
				});
			}

			// amount adding tax
			totalPrice = totalPrice + (totalPrice * (5 / 100));

			// amount adding shipping
			totalPrice = totalPrice + (totalPrice * (10 / 100));

			// Rounding after decimal to two digit
			totalPrice = Math.round(totalPrice * 100.0) / 100.0;
			
			order.setOrderTotal(totalPrice);

			// payment
			payment.setAmount(totalPrice);
			payment.setOrderId(order.getOrderId());
			payment.setStatus("SUCCESS");
			payment.setCreatedAt(LocalDateTime.now());
			payment.setMethod("UPI");// Assume using UPI
			payment.setReference(generatePaymentTracking());

			ResponseEntity<Payment> paymentResponse = paymentClient.createpayment(payment, idempotencyKey);
			if (paymentResponse.getStatusCode().is2xxSuccessful()) {
				System.out.println(" Request successful!");
				// response body
				payment = (Payment) paymentResponse.getBody();
			} else {
				order.setPaymentStatus("FAILED");
				paymentsFailed.increment();
			}
			order.setPaymentStatus(payment.getStatus());
			orderRepository.save(order);
			if (payment.getStatus().equalsIgnoreCase("SUCCESS")) {
				this.releaseProduct(order, true);
				// metric
				ordersPlaced.increment();
			} else {
				orderFailed.increment();
				this.releaseProduct(order, false);
				return;
			}
			
			// shipment
			shipment.setOrderId(order.getOrderId());
			shipment.setCarrier("DHL");// Assume DHL logistic service
			shipment.setShippedAt(LocalDateTime.now());
			shipment.setStatus("ORDER PLACED");
			shipment.setTrackingNo(generateTrackingNumber());

			// shipmentClient.
			ResponseEntity<Shipment> shipmentResponse = shipmentClient.createShipment(shipment);
			log.info("Order shipped successfully");
			if (paymentResponse.getStatusCode().is2xxSuccessful()) {
				System.out.println(" Request successful!");
				shipment = (Shipment) shipmentResponse.getBody();
			
				// Call API for Customer
				Customer customer = null;
				ResponseEntity<Customer> customerResponse = null;
				 try {
					 customerResponse = customerClient.getCustomerById(order.getCustomerId());
				}catch(Exception e) {
			 		log.error("Could not get Customer details");
			 	}
				if (customerResponse!=null && paymentResponse.getStatusCode().is2xxSuccessful()) {
					System.out.println(" Request successful!");
					customer = (Customer) customerResponse.getBody();
				} else {
					order.setPaymentStatus("FAILED");
				}

				if (customer != null && customer.getEmail() != null) {
					try {
						Notification notif = new Notification();
						notif.setToEmail(customer.getEmail());
						notif.setSubject("Order submitted successfully");
						notif.setBody("Thank you for your order. You will track shipment using Tracking:"
								+ shipment.getTrackingNo());
						// Call API for Send notification
						notificationClient.sendNotification(notif);
					} catch (Exception e) {
						log.error("Notification send failed");
					}
				} else {
					log.warn("Customer not avialable");
				}
			} 
			
			log.info("Order {} is created successfully", order.getOrderId());
		} catch (Exception e) {
			log.error("Order creation failed", e);
			// payment failure/cancel
			// Shipment cancel
			// release inventory
			this.releaseProduct(order, false);

			orderFailed.increment();

			throw e;
		}
	}

	@Transactional
	@Override
	public void updateOrder(Order order) throws OrderException {
		try {
			for (OrderItem item : order.getItems()) {
				boolean inStock = inventoryClient.isInStock(item.getProductId(), item.getQuantity());
				if (!inStock) {
					throw new OrderException("Product " + item.getProductId() + " is not in stock");
				}
			}
			orderRepository.save(order);
			log.info("Order {} is updated successfully", order.getOrderId());
		} catch (Exception e) {
			log.error("Order updation failed");
			throw e;
		}
	}

	@Transactional
	@Override
	public void deleteOrder(int productId) throws OrderException {
		try {
			orderRepository.deleteById(productId);
			log.info("Order {} is deleted successfully", productId);
		} catch (Exception e) {
			log.error("Order deletion failed");
			throw e;
		}
	}

	private void releaseProduct(Order order, boolean relaseType) {
		// Release product in inventory
		for (OrderItem item : order.getItems()) {
			try {
				inventoryClient.releaseProducct(item.getProductId(), relaseType);
			} catch (Exception e) {
				log.error("Product release failed");
			}
		}
	}

	// Shipment tracking reference generator
	static String generateTrackingNumber() {
		Random random = new Random();
		int numberPart = 1000 + random.nextInt(9000); // generates a 4-digit number
		return "TRK" + numberPart;
	}

	// Payment tracking reference generator
	static String generatePaymentTracking() {
		String prefix = "ECI";// Prefix
		// 2. Current date in yyyyMMdd format
		String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());// date in yyyyMMdd format
		String randomPart = generateRandomAlphanumeric(4);
		return prefix + datePart + "-ID" + randomPart;
	}

	// Helper method to generate random alphanumeric strings
	static String generateRandomAlphanumeric(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}
		return sb.toString();
	}
	/*
	 * @CircuitBreaker(name = "instock", fallbackMethod = "fallbackStockRCMethod")
	 * 
	 * @Retry(name="instock") public Boolean isInStock(Integer productId, Integer
	 * quantity) { Boolean isInStock = restClient.get() .uri(uriBuilder ->
	 * uriBuilder .path("/api/v1/inventory/product/instock")
	 * .queryParam("productId", productId) .queryParam("quantity", quantity)
	 * .build()) .retrieve() .toEntity(Boolean.class) // returns
	 * ResponseEntity<Boolean> .getBody(); if(isInStock)
	 * log.info("Product in stock"); else log.info("Product not in stock"); return
	 * isInStock; }
	 * 
	 * @CircuitBreaker(name = "reserve", fallbackMethod = "fallbackReserveCMethod")
	 * 
	 * @Retry(name="reserve") public Boolean reserveProducct(Integer productId) {
	 * Boolean reserveStatus = restClient.get() .uri(uriBuilder -> uriBuilder
	 * .path("/api/v1/inventory/product/reserve") .queryParam("productId",
	 * productId) .build()) .retrieve() .toEntity(Boolean.class) .getBody();
	 * if(reserveStatus) log.info("Product reserve successfully"); else
	 * log.info("Product reserve failed"); return reserveStatus; }
	 * 
	 * @CircuitBreaker(name = "release", fallbackMethod = "fallbackReserveCMethod")
	 * 
	 * @Retry(name="release") public Boolean releaseProducct(Integer productId) {
	 * Boolean releaseStatus = restClient.get() .uri(uriBuilder -> uriBuilder
	 * .path("/api/v1/inventory/product/release") .queryParam("productId",
	 * productId) .build()) .retrieve() .toEntity(Boolean.class) .getBody();
	 * 
	 * if(releaseStatus) log.info("Product release successfully"); else
	 * log.info("Product release failed");
	 * 
	 * return releaseStatus; }
	 * 
	 * @CircuitBreaker(name = "sendNotif", fallbackMethod = "notificationFallback")
	 * 
	 * @Retry(name="sendNotif") public Boolean sendNotification(Notification user) {
	 * 
	 * Boolean notifStatus = restClient.post() .uri("/api/v1/notification")
	 * .body(user) .retrieve() .body(Boolean.class);
	 * 
	 * if(notifStatus) log.info("Email notficiation sent successfully"); else
	 * log.info("Email notficiation sent failed");
	 * 
	 * return notifStatus; }
	 * 
	 * @CircuitBreaker(name = "customer", fallbackMethod = "fallbackStockRCMethod")
	 * 
	 * @Retry(name="customer") public Customer getCustomerById(Integer customerId) {
	 * Customer customer = restClient.get() .uri(uriBuilder -> uriBuilder
	 * .path("/api/v1/customerId/{customerId}") .build(Map.of("{customerId}",
	 * customerId))) // pass path variable //.queryParam("productId", productId)
	 * //.build()) .retrieve() .toEntity(Customer.class) .getBody(); if(customer !=
	 * null) { log.info("Customer avaliable successfully"); }else {
	 * log.info("Customer not avialable successfully"); } return customer; }
	 * 
	 * // Fallbacks boolean fallbackStockRCMethod(Integer productId, Integer
	 * quantity, Throwable t) { log.info("Cannot get inventory for productId:{}"+
	 * t.getMessage(),productId); return false; } boolean
	 * fallbackReserveCMethod(Integer productId, Throwable t) {
	 * log.info("Cannot reserve/release lock on productId {}"+
	 * t.getMessage(),productId); return false; }
	 * 
	 * public boolean notificationFallback(Notification notification, Throwable ex)
	 * { log.info(" Send notificaiton '{}' fallback triggered: " + ex.getMessage(),
	 * notification.getToEmail()); return false; } public boolean
	 * customerFallback(Customer user, Throwable ex) {
	 * log.info(" Get customer fallback triggered: " + ex.getMessage()); return
	 * false; }
	 */
}
