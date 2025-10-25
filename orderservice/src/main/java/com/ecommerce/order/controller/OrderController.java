package com.ecommerce.order.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.exception.OrderException;
import com.ecommerce.order.pojo.IdempotencyResponse;
import com.ecommerce.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**Order Service Rest controller
 * 
 * @author Suresh Injeti
 *
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	
	//To submit multiple requests will same response
	public static HashMap<String, IdempotencyResponse> idempotencyKeyMap = new HashMap<>();

	
	@Autowired
	private OrderService orderService;
	
	//Create order with idempotency key support
	@PostMapping
	public ResponseEntity<Object> createOrder(@Valid @RequestBody Order order, 
			@RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey, BindingResult bindingResult) {
		log.info("Order createtion started");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Validation failed");
		}
	    if (idempotencyKey == null || idempotencyKey.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Missing Order Idempotency-Key header");
	    }
	    
	    if (idempotencyKeyMap.containsKey(idempotencyKey)) {
	    	IdempotencyResponse response = idempotencyKeyMap.get(idempotencyKey);
	    	log.info("Returning cached Order response for Idempotency-Key: {}", idempotencyKey);
	        return new ResponseEntity<>(response.getResponseBody(), response.getResponseStatus());
	    }
	    IdempotencyResponse idemtepotenyRes = new IdempotencyResponse();
		try {
			orderService.createOrder(order);
			idemtepotenyRes.setResponseStatus(HttpStatus.CREATED);
			idemtepotenyRes.setResponseBody(order.toString());
			log.info("Order createtion ends");
		}catch(OrderException pe) {
			idemtepotenyRes.setResponseStatus(HttpStatus.NOT_FOUND);
			idemtepotenyRes.setResponseBody(pe.getMessage());
			log.error("Failed to create Order");
		}
		return new ResponseEntity<Object>(idemtepotenyRes.getResponseBody(),  idemtepotenyRes.getResponseStatus());
		
	}

	// This method gets the order details by orderid
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOrderById(@Valid @PathVariable("id") Integer orderId) {

		try {
			Order order = orderService.getOrderById(orderId);
			return new ResponseEntity<Object>(order, HttpStatus.FOUND);
		}catch(OrderException oe) {
			log.error("Failed to get order");
			return new ResponseEntity<Object>(oe.getMessage(),  HttpStatus.NOT_FOUND);
		}

	}

	// This method will delete the order from list
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrderById(@Valid @PathVariable("id") Integer orderId) {
		try {
		orderService.deleteOrder(orderId);
		return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);

		}catch(OrderException pe) {
			log.error("Failed to delete order");
			return new ResponseEntity<String>(pe.getMessage(),  HttpStatus.NOT_FOUND);
		}
	}

	//Update order details
	@PutMapping
	public ResponseEntity<Object> updateOrder(@Valid @RequestBody Order order, BindingResult bindingResult) {
		log.info("Order updation started");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Validation failed");
		}
		try {
			orderService.updateOrder(order);
			log.info("Order updation ends");
			return new ResponseEntity<Object>(order, HttpStatus.ACCEPTED);
		}catch(OrderException pe) {
			log.error("Failed to update order");
			return new ResponseEntity<Object>(pe.getMessage(),  HttpStatus.NOT_FOUND);
		}
	}

	//Get all orders
	@GetMapping
	public ResponseEntity<Object> getAllOrders() {
		try {
			List<Order> list = orderService.getAllOrders();
			return new ResponseEntity<Object>(list, HttpStatus.OK);
		} catch (OrderException pe) {
			log.error("Failed to get all orders");
			return new ResponseEntity<Object>(pe.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}