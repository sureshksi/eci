package com.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.order.entity.OrderItem;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	@Query("SELECT max(ot.orderItemId) FROM OrderItem ot")
	int getOrderItemIncrement();
	
    @Modifying
    @Query("UPDATE OrderItem i SET i.isdeleted = true WHERE i.order_id= :orderId")
    void deleteByOrderId(@Param("orderId") Integer orderId);

}
