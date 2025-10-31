package com.ecommerce.order.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.ecommerce.order.pojo.Payment;


public interface PaymentClient {

	@PutExchange("/api/v1/payment/{orderId}/refund")
	ResponseEntity<Payment> refundPayment(@PathVariable Integer orderId, @RequestHeader(value = "Idempotency-Key") String idempotencyKey);
	
	@PostExchange("/api/v1/payment/charge")
	ResponseEntity<Payment> createpayment(@RequestBody Payment shipment, @RequestHeader(value = "Idempotency-Key") String idempotencyKey);
}
