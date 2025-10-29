package com.ecommerce.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.shipping.model.Shipment;

/**Product Service Rest controller
 * 
 * @author Gokul G Gowda
 *
 */

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
	
    @Modifying
    @Query("UPDATE Shipment i SET i.status = :status, i.deliveredAt = CURRENT_TIMESTAMP  WHERE i.shipmentId= :shipmentId")
    void updateStatus(@Param("status") String status, @Param("shipmentId") Integer shipmentId);

    @Query("SELECT s FROM Shipment s WHERE s.trackingNo= :trackingId")
    Shipment getTrackStatus(@Param("trackingId") String trackingId);
}
