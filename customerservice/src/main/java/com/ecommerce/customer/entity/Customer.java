package com.ecommerce.customer.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**Entity for Customer service
 * 
 * @author Suresh Injeti
 *
 */
@Data
@RequiredArgsConstructor
@Entity
@Table(name="customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="customer_id")
	private Integer cutomerId;
	private String name;
	private String email;
	private String phone;
	
	@CreationTimestamp
	private Timestamp created_at;

}
