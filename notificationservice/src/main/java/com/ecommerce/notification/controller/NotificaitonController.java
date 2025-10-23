package com.ecommerce.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.notification.exception.NotificationException;
import com.ecommerce.notification.pojo.Notification;
import com.ecommerce.notification.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificaitonController {

	@Autowired
	NotificationService notificaitonService;
	
	@PostMapping
	public ResponseEntity<String> sendNotification(@Valid @RequestBody Notification notificaiton, BindingResult bindingResult) {
		log.info("Notificaiton createtion started");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("Validation failed");
		}
		try {
			notificaitonService.sendEmail(notificaiton);
		 log.info("Notificaiton createtion ends");
		 return new ResponseEntity<String>("Email sent success", HttpStatus.CREATED);

		}catch(NotificationException pe) {
			log.error("Failed to send email");
			return new ResponseEntity<String>("Email sent failed",  HttpStatus.NOT_FOUND);
		}
		
	}
}
