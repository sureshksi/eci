package com.ecommerce.notification.pojo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Notification {
	
	private String toEmail;
	private String subject;
	private String body; 
	private String ccEmail;

}
