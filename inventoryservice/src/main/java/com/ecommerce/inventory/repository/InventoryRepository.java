package com.ecommerce.inventory.repository;

import java.time.LocalDateTime;
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
	
	@Query("SELECT i FROM Inventory i WHERE i.productId = :productId")
	Optional<Inventory> getProductById(@Param("productId") Integer productId);
	
	@Modifying
	@Query("UPDATE Inventory i SET i.updatedAt= :updatedAt, i.onHand= :onHand, i.reserved = :reserved WHERE i.productId = :productId")
	void reservedByProductId(@Param("productId") Integer productId, @Param("onHand") Integer onHand, @Param("reserved") Integer reserved, @Param("updatedAt") LocalDateTime updatedAt);
	
    boolean existsByProductIdAndOnHandGreaterThanEqual(Integer productId, Integer quantity);
    
    @Modifying
    @Query("UPDATE Inventory i SET i.onHand = i.onHand + i.reserved, i.reserved = 0  WHERE i.reserved > 0   AND i.updatedAt < :cutoff")
    void releaseExpiredReservations(@Param("cutoff") LocalDateTime cutoff);

    @Modifying
	@Query("UPDATE Inventory i SET i.updatedAt= :updatedAt, i.onHand= :onHand WHERE i.productId = :productId")
	void cancelByProductId(@Param("productId") Integer productId, @Param("onHand") Integer onHand, @Param("updatedAt") LocalDateTime updatedAt);
}
