package com.ecommerce.order.pojo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Payment {
	private Integer paymentId;
    private Integer orderId;
	private Double amount;
	private String method;
	private String status;
	private String reference;
	private LocalDateTime  createdAt;
}
