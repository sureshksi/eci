package com.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.order.entity.Order;

/**Order Service Repository
 * 
 * @author Suresh Ineti
 *
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	@Query("SELECT max(o.orderId) FROM Order o")
	int getOrderIncrement();
	
    @Modifying
    @Query("UPDATE Order i SET i.isdeleted = true WHERE i.orderId= :orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);

}
