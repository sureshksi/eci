package com.ecommerce.notification.service;

import org.springframework.stereotype.Component;

import com.ecommerce.notification.exception.NotificationException;
import com.ecommerce.notification.pojo.Notification;

public interface NotificationService {

	public void sendEmail(Notification notificaiton) throws NotificationException;
}
