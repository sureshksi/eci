package com.ecommerce.order.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.ecommerce.order.pojo.Payment;

public interface PaymentClient {

	@PatchExchange("/api/v1/payment/updateStatus")
	ResponseEntity<?> updatePaymentStatus(@RequestParam Integer shippingId, @RequestParam String shippingStatus);
	
	@PostExchange("/api/v1/payment")
	ResponseEntity<?> createpayment(@RequestBody Payment shipment);
}
