package com.ecommerce.order.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.ecommerce.order.pojo.Notification;

/**Notification client service call
 * 
 * @author Suresh Injeti
 *
 */
public interface NotificationClient {

	@PostExchange("/api/v1/notification")
    boolean sendNotification(@RequestBody Notification notificaiton); 
 
    default boolean notificationFallback(Notification notification, Throwable ex) {
        //log.info(" Send notificaiton '{}' fallback triggered: " + ex.getMessage(), notification.getToEmail());
        return false;
    }

    
}
