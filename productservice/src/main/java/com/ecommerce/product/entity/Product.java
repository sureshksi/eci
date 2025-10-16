package com.ecommerce.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**Product Service entity
 * 
 * @author Suresh Injeti
 *
 */
@Entity
@Data
@Table(name="products")
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	@NotBlank(message = "SKU cannot be null or empty")
	private String sku;
	@NotBlank(message = "Name cannot be null or empty")
	private String name;
	@NotBlank(message = "Category cannot be null empty")
	private String category;
	@NotNull(message = "Price cannot be null")
	private Double price;
	private String is_active="True";

}
