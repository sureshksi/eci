package com.ecommerce.order.pojo;

import java.sql.Timestamp;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Customer {

	private Integer cutomerId;
	private String name;
	private String email;
	private String phone;
	private Timestamp created_at;
}
