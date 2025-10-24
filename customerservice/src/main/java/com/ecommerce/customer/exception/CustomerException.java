package com.ecommerce.customer.exception;

/**Customer Service Customized exception
 * 
 * @author Suresh Injeti
 *
 */
@SuppressWarnings("serial")
public class CustomerException extends Exception{

	public CustomerException(){
		super();
	}
	public CustomerException(String message){
		super(message);
	}
}
