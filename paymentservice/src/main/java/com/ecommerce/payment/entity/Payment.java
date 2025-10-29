package com.ecommerce.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name="payments")
public class Payment {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="payment_id")
	private Integer paymentId;
    @Column(name="order_id")
	private Integer orderId;
	private Double amount;
	private String method;
	private String status;
	private String reference;
	@Column(name="created_at")
	private LocalDateTime  createdAt;
}
