package com.ecommerce.order.exception;

/**Order Service Customized exception
 * 
 * @author Suresh Injeti
 *
 */
@SuppressWarnings("serial")
public class OrderException extends Exception{

	public OrderException(){
		super();
	}
	public OrderException(String message){
		super(message);
	}
}
