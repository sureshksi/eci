package com.ecommerce.inventory.entity;

import java.sql.Timestamp;

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

/**Inventory Service entity
 * 
 * @author Suresh Injeti
 *
 */
@Entity
@Data
@Table(name="inventory")
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_id")
	private Integer inventoryId;
	
	@NotNull(message = "Product id cannot be null")
	@Column(name = "product_id")
	private Integer productId;
	
	@NotBlank(message = "Warehouse cannot be null or empty")
	private String warehouse;
	
	@NotNull(message = "On hand value cannot be null")
	@Column(name = "on_hand")
	private Integer onHand;
	
	@NotNull(message = "Reserved cannot be null")
	private Integer reserved;
	
	@Column(name = "updated_at")
	private Timestamp updatedAt;
	
}
