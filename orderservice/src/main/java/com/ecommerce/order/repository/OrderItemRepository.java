package com.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.order.entity.OrderItem;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	@Query("SELECT max(ot.orderItemId) FROM OrderItem ot")
	int getOrderItemIncrement();
}
