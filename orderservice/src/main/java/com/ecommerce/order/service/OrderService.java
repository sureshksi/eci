package com.ecommerce.order.service;

import java.util.List;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.exception.OrderException;

/**Product Service class
 * 
 * @author Suresh Ineti
 *
 */
public interface OrderService {
	
	public List<Order> getAllOrders() throws OrderException;
	public Order getOrderById(int orderId) throws OrderException;
	public void createOrder(Order order) throws OrderException;
	public void updateOrder(Order order) throws OrderException;
	public void deleteOrder(int productId) throws OrderException;

}
