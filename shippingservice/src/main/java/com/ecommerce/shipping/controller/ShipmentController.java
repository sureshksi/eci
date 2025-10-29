package com.ecommerce.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.shipping.model.Shipment;
import com.ecommerce.shipping.service.ShipmentService;

import java.util.List;

/**Product Service Rest controller
 * 
 * @author Gokul G Gowda
 *
 */


@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

	@Autowired
    private ShipmentService shipmentService;

//    public ShipmentController(ShipmentService shipmentService) {
//        this.shipmentService = shipmentService;
//    }

    @GetMapping
    public List<Shipment> getAll() {
        return shipmentService.getAllShipments();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
    	Shipment shipment = shipmentService.getShipmentById(id);
    	return ResponseEntity.ok(shipment);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Shipment shipment) {
    	 shipmentService.createShipment(shipment);
    	 return ResponseEntity.ok(shipment);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Shipment shipment) {
        shipmentService.updateShipment(shipment);
        return ResponseEntity.ok(shipment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.ok("Shipping record deleted successfully");
    }
    
    @PatchMapping("/updateStatus")
    public ResponseEntity<?> updateShippingStatus(@RequestParam Integer shippingId, @RequestParam String shippingStatus) {
    	shipmentService.updateShipmentStatus(shippingId, shippingStatus);
    	return ResponseEntity.ok("Shipping status updated successfully");
    }
    
    @GetMapping("/track/{trackingId}")
    public ResponseEntity<?> getShippingStatusByTrackId(@PathVariable String trackingId) {
    	Shipment shipment = shipmentService.getShipmentStausByTrackId(trackingId);
    	return ResponseEntity.ok(shipment);
    }
}
