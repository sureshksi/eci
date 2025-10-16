package com.ecommerce.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**OrderItem Service entity
 * 
 * @author Suresh Injeti
 *
 */
@Entity
@Data
@Table(name="order_items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Integer orderItemId;
	
//	@Column(name = "order_id")
    private Integer order_id;

   // private Order order;
    
	@NotNull(message = "Product id cannot be null")
	@Column(name = "product_id")
	private Integer productId;
	
	@NotBlank(message = "SKU cannot be null or empty")
	private String sku;
	
	@NotNull(message = "Quantity id cannot be null")
	private Integer quantity;
	
	@NotNull(message = "Unit price cannot be null")
	@Column(name = "unit_price")
	private Double unitPrice;
	
}
