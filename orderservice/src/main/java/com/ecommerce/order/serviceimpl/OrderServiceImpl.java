package com.ecommerce.order.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.order.client.InventoryClient;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.exception.OrderException;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**Order Service  class
 * 
 * @author Suresh Injeti
 *
 */
@Service
//@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private  OrderRepository orderRepository;
	
	@Autowired
	 private InventoryClient inventoryClient;
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	private final MeterRegistry meterRegistry;
	
	private Timer reserveInventoryTimer;
    private Counter ordersPlaced;
    private Counter orderFailed;
    private Counter paymentsFailed;
    private Counter stockouts;

    public OrderServiceImpl(MeterRegistry registry) {
        this.meterRegistry = registry;
        this.ordersPlaced = Counter.builder("orders_placed_total")
                                   .tag("service", "order-service")
                                   .register(registry);
        this.orderFailed = Counter.builder("orders_placed_failed")
                .tag("service", "order-service")
                .register(registry);
        this.paymentsFailed = Counter.builder("payments_failed_total")
                                     .tag("service", "order-service")
                                     .register(registry);
        this.stockouts = Counter.builder("stockouts_total")
                                .tag("service", "order-service")
                                .register(registry);

        this.reserveInventoryTimer = Timer.builder("inventory_reserve_latency_ms")
                                          .description("Latency for inventory reservation")
                                          .tag("service", "order-service")
                                          .publishPercentiles(0.5, 0.95)   // optional
                                          .register(registry);
    }
	@Override
	public List<Order> getAllOrders() throws OrderException {
		List<Order> orderList = orderRepository.findAll();
		if(orderList == null || orderList.isEmpty())
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
		try {
			int index = orderRepository.getOrderIncrement();
			int itemIndex = orderItemRepository.getOrderItemIncrement();
			index = index+1;
			order.setOrderId(index);
			double totalPrice = 0.0;
			//(Σ unit_price × qty + 5% tax + shipping)
			for(OrderItem item : order.getItems()) {
				boolean inStock = inventoryClient.isInStock(item.getProductId(), item.getQuantity());
		        if (!inStock) {
		            stockouts.increment();
		        	meterRegistry.counter("stockouts_total").increment();
		            throw new OrderException("Product is not in stock. Please try again");
		        }
				itemIndex=itemIndex+1;
				item.setOrderItemId(itemIndex);
				item.setOrder_id(index);
				totalPrice = totalPrice+(item.getUnitPrice()*item.getQuantity());
				
				 //latency
	            reserveInventoryTimer.record(() -> {
	            	//Reserve product in inventory
					inventoryClient.reserveProducct(item.getProductId());
	            });				
			}
			
			//amount adding tax
			totalPrice = totalPrice+(totalPrice *(5/100));
			
			//amount adding shipping
			totalPrice = totalPrice+(totalPrice *(10/100));
			
			//payment
			
			
			orderRepository.save(order);
			//metric
			ordersPlaced.increment();
			
			//shipment
			
			log.info("Order {} is created successfully", order.getOrderId());
		}catch(Exception e) {
			log.error("Order creation failed");
			this.releaseProduct(order);
			orderFailed.increment();
			
			throw e;
		}
	}

	@Transactional
	@Override
	public void updateOrder(Order order) throws OrderException {
		try {
			for(OrderItem item : order.getItems()) {
				boolean inStock = inventoryClient.isInStock(item.getProductId(), item.getQuantity());
		        if (!inStock) {
		            throw new OrderException("Product " + item.getProductId() + " is not in stock");
		        }
			}
			orderRepository.save(order);
			log.info("Order {} is updated successfully", order.getOrderId());
		}catch(Exception e) {
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
	
	private void releaseProduct(Order order) {
		//Release product in inventory
		for(OrderItem item : order.getItems()) {
			try {
				inventoryClient.releaseProducct(item.getProductId());
			}catch(Exception e) {
				log.error("Product release failed");
			}
	    }
	}

}
