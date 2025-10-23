package com.ecommerce.notification.exception;

@SuppressWarnings("serial")
public class NotificationException extends Exception{
	
	public NotificationException(){
		super();
	}

	public NotificationException(String message){
		super(message);
	}
}
