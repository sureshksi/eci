package com.ecommerce.payment.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.exception.PaymentException;
import com.ecommerce.payment.pojo.IdempotencyResponse;
import com.ecommerce.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

	@Autowired
	PaymentService paymentService;
	
	//To submit multiple requests will same response
	public static HashMap<String, IdempotencyResponse> idempotencyKeyMap = new HashMap<>();

	 @GetMapping
	    public List<Payment> getAll() {
	        return paymentService.getAllPayments();
	    }
	    @GetMapping("/{id}")
	    public ResponseEntity<?> getById(@PathVariable Integer id) {
	    	Payment payment = paymentService.getPaymentById(id);
	    	return ResponseEntity.ok(payment);
	    }

	    @GetMapping("/order/{orderId}")
	    public ResponseEntity<?> getByOrderId(@PathVariable Integer orderId) {
	    	Payment payment = paymentService.getPaymentByOrderId(orderId);
	    	return ResponseEntity.ok(payment);
	    }
	    
	    @PostMapping("/charge")
	    public ResponseEntity<?> create(@RequestBody Payment payment, @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
	    	
	    	if (idempotencyKey == null || idempotencyKey.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                .body("Missing Payment Idempotency-Key header");
		    }
		    
		    if (idempotencyKeyMap.containsKey(idempotencyKey)) {
		    	IdempotencyResponse response = idempotencyKeyMap.get(idempotencyKey);
		    	
		        log.info("Returning cached Payment response for Idempotency-Key: {}", idempotencyKey);
		        return new ResponseEntity<>(response.getResponseBody(), response.getResponseStatus());
		    }
		    IdempotencyResponse idemtepotenyRes = new IdempotencyResponse();
		    
	    	try {
	    		//UPI and COD do not add any charge
	    		if(payment.getMethod().equalsIgnoreCase("CARD")) {
	    			double toalAmount = payment.getAmount()+(payment.getAmount()*(2.50/100.0));
		    		//Making rounding to two digits after decimal	
	    			 toalAmount =	Math.round(toalAmount * 100.0) / 100.0;
	    			 payment.setAmount(toalAmount);
	    		}
		    	paymentService.createPayment(payment);
		    	idemtepotenyRes.setResponseStatus(HttpStatus.CREATED);
				idemtepotenyRes.setResponseBody(payment.toString());
			}catch(Exception e) {
				idemtepotenyRes.setResponseStatus(HttpStatus.NOT_FOUND);
				idemtepotenyRes.setResponseBody(e.getMessage());
				log.error("Failed to create payment");
				return new ResponseEntity<>("Create record failed",  HttpStatus.NOT_FOUND);
	    	}
	    	idempotencyKeyMap.put(idempotencyKey, idemtepotenyRes);
	    	return new ResponseEntity<>(payment, idemtepotenyRes.getResponseStatus());
	    }
	    
	    @PutMapping("/{orderId}/refund")
	    public ResponseEntity<?> refund(@PathVariable Integer orderId, @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
	        IdempotencyResponse idemtepotenyRes = new IdempotencyResponse();
	        Payment payment =  new Payment();
	    	try {
		    	if (idempotencyKey == null || idempotencyKey.isEmpty()) {
			        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			                .body("Missing Payment Idempotency-Key header");
			    }
			    
			    if (idempotencyKeyMap.containsKey(idempotencyKey)) {
			    	IdempotencyResponse response = idempotencyKeyMap.get(idempotencyKey);
			    	
			        log.info("Returning cached Payment response for Idempotency-Key: {}", idempotencyKey);
			        return new ResponseEntity<>(response.getResponseBody(), response.getResponseStatus());
			    }
			payment =  paymentService.getPaymentByOrderId(orderId);
			payment.setStatus("REFUND");
			paymentService.updatePayment(payment);
	    	idemtepotenyRes.setResponseStatus(HttpStatus.OK);
			idemtepotenyRes.setResponseBody(payment.toString());
		}catch(Exception e) {
			idemtepotenyRes.setResponseStatus(HttpStatus.NOT_FOUND);
			idemtepotenyRes.setResponseBody(e.getMessage());
	    		return new ResponseEntity<>("Refund record failed",  HttpStatus.NOT_FOUND);
	    	}
	    	idempotencyKeyMap.put(idempotencyKey, idemtepotenyRes);
	    	return new ResponseEntity<>(payment, idemtepotenyRes.getResponseStatus());
	    }

	    @PutMapping
	    public ResponseEntity<?> update(@RequestBody Payment payment) {
	    	try {
	    	paymentService.updatePayment(payment);
	        return ResponseEntity.ok(payment);
	    	}catch(PaymentException pe) {
	    		return new ResponseEntity<>("Update record failed",  HttpStatus.NOT_FOUND);
	    	}
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> delete(@PathVariable Integer id) {
	    	try {
	    	paymentService.deletePayment(id);
	        return ResponseEntity.ok("Payment record deleted successfully");
	    	}catch(PaymentException pe) {
	    		return new ResponseEntity<>("delete record failed",  HttpStatus.NOT_FOUND);
	    	}
	    }
	    
	    @PatchMapping("/updateStatus")
	    public ResponseEntity<?> updatePaymentStatus(@RequestParam Integer paymentId, @RequestParam String paymentStatus) {
	    	try {
	    	paymentService.updatePaymentStatus(paymentId, paymentStatus);
	    	return ResponseEntity.ok("Payment status updated successfully");
	    	}catch(PaymentException pe) {
	    		return new ResponseEntity<>("Payent status update record failed",  HttpStatus.NOT_FOUND);
	    	}
	    }

	    @GetMapping("/track/{trackingId}")
	    public ResponseEntity<?> getPaymentStatusByTrackId(@PathVariable String trackingId) {
	    	Payment payment = paymentService.getPaymentStausByTrackId(trackingId);
	    	return ResponseEntity.ok(payment);
	    }
}
