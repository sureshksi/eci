package com.ecommerce.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.inventory.entity.Inventory;

/**Inventory Service Repository
 * 
 * @author Suresh Injeti
 *
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
	
	@Query("SELECT pc FROM Inventory pc WHERE pc.productId = :productId")
	Optional<Inventory> getProductById(@Param("productId") Integer productId);
	
	@Modifying
	@Query("UPDATE Inventory pc SET pc.reserved = :reserved WHERE pc.productId = :productId")
	void reservedByProductId(@Param("productId") Integer productId, @Param("reserved") Integer reserved);
	
    boolean existsByProductIdAndOnHandGreaterThanEqual(Integer productId, Integer quantity);
    
}
