package com.ecommerce.shipping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.shipping.model.Shipment;
import com.ecommerce.shipping.repository.ShipmentRepository;

import jakarta.transaction.Transactional;

/**Product Service Rest controller
 * 
 * @author Gokul G Gowda
 *
 */


@Service
public class ShipmentService {

	@Autowired
    private ShipmentRepository shipmentRepository;

//    public ShipmentService(ShipmentRepository shipmentRepository) {
//        this.shipmentRepository = shipmentRepository;
//    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment getShipmentById(Integer id) {
        return shipmentRepository.findById(id).orElse(null);
    }

    public Shipment createShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public void deleteShipment(Integer id) {
        shipmentRepository.deleteById(id);
    }
    
    @Transactional
    public void updateShipmentStatus(Integer shipmentId, String status) {
        shipmentRepository.updateStatus(status, shipmentId);
    }
    
    public Shipment getShipmentStausByTrackId(String trackId) {
        return shipmentRepository.getTrackStatus(trackId);
    }
}
