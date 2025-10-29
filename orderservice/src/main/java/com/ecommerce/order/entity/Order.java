package com.ecommerce.order.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**Order Service entity
 * 
 * @author Suresh Injeti
 *
 */
@Entity
@Data
@Table(name="orders")
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Order {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId;
	
	@NotNull(message = "Customer Id cannot be null")
	@Column(name = "customer_id")
	private Integer customerId;
	
	@NotBlank(message = "Order status cannot be null or empty")
	@Column(name = "order_status")
	private String orderStatus;
	
	//@NotBlank(message = "Payment status cannot be null or empty")
	@Column(name = "payment_status")
	private String paymentStatus;
	
	@NotNull(message = "Order total cannot be null")
	@Column(name = "order_total")
	private Double orderTotal;
	
	@CreationTimestamp
	@Column(name = "created_at")
	private Timestamp createdAt;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id") 
    private List<OrderItem> items = new ArrayList<>();
}
