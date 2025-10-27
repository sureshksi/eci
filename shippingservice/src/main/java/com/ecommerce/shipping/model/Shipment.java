package com.ecommerce.shipping.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**Product Service Rest controller
 * 
 * @author Gokul G Gowda
 *
 */

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shipment_id")
    private Integer shipmentId;
    @Column(name="order_id")
    private Long orderId;
    private String carrier;
    private String status;
    @Column(name="tracking_no")
    private String trackingNo;
    @Column(name="shipped_at")
    private LocalDateTime shippedAt;
    @Column(name="delivered_at")
    private LocalDateTime deliveredAt;

    // Getters & Setters
    public Integer getShipmentId() { return shipmentId; }
    public void setShipmentId(Integer shipmentId) { this.shipmentId = shipmentId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }

    public LocalDateTime getShippedAt() { return shippedAt; }
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
}
