package com.ecommerce.order.pojo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Shipment {
    private Integer shipmentId;
    private Integer orderId;
    private String carrier;
    private String status;
    private String trackingNo;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
}
