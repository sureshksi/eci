package com.ecommerce.product.exception;

/**Product Service Customized exception
 * 
 * @author Suresh Injeti
 *
 */
@SuppressWarnings("serial")
public class ProductException extends Exception{

	public ProductException(){
		super();
	}
	public ProductException(String message){
		super(message);
	}
}
