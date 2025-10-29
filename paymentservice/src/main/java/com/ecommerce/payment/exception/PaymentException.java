package com.ecommerce.payment.exception;

/**Payment Service Customized exception
 * 
 * @author Suresh Injeti
 *
 */
@SuppressWarnings("serial")
public class PaymentException extends Exception{

	public PaymentException(){
		super();
	}
	public PaymentException(String message){
		super(message);
	}
}
