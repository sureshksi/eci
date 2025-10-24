package com.ecommerce.order.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.ecommerce.order.pojo.Notification;

public interface NotificationClient {

	@PostExchange("/api/v1/notification")
    boolean sendNotification(@RequestBody Notification notificaiton); 
 
}
