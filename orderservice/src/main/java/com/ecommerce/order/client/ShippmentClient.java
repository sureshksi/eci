package com.ecommerce.order.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.ecommerce.order.pojo.Shipment;

public interface ShippmentClient {
	//		        /api/v1/shipments/updateStatus/order?orderId=131&shippingStatus=Cancelled
	@PatchExchange("/api/v1/shipments/updateStatus/order")
	ResponseEntity<String> updateShippingStatus(@RequestParam Integer orderId, @RequestParam String shippingStatus);
	
	@PostExchange("/api/v1/shipments")
	ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment);

}
