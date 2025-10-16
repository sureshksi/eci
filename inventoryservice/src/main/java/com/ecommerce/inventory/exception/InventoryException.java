package com.ecommerce.inventory.exception;

/**Inventory Service Customized exception
 * 
 * @author Suresh Injeti
 *
 */
@SuppressWarnings("serial")
public class InventoryException extends Exception{

	public InventoryException(){
		super();
	}
	public InventoryException(String message){
		super(message);
	}
}
