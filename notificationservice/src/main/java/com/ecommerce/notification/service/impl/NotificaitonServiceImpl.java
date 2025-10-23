package com.ecommerce.notification.service.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.notification.exception.NotificationException;
import com.ecommerce.notification.pojo.Notification;
import com.ecommerce.notification.service.NotificationService;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class NotificaitonServiceImpl implements NotificationService {
	@Value("${app.email.host}")
	private String emailHost;
	@Value("${app.email.port}")
	private int emailPort=587;
	@Value("${app.email.usessl}")
	private boolean useSSL;
	@Value("${app.email.auth.required}")
	private boolean authReq;
	@Value("${app.email.login}")
	private String userName;
	@Value("${app.email.pwd}")
	private String emailPwd;
	@Value("${app.email.from.address}")
	private String fromEmail;
	
	@Override
	public void sendEmail(Notification notificaiton) throws NotificationException {
		log.info("Entered into sendEmail method");
		try {
			log.debug("toEmail=" + notificaiton.toString());
			
			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.host", emailHost);
			mailProps.put("mail.smtp.port", emailPort);// 587
			if (useSSL) {
				mailProps.put("mail.smtp.ssl.enable", useSSL);
			} else {
				mailProps.put("mail.smtp.starttls.enable", "true");
			}
			// Authentication
			Session session = null;
			if (authReq) {
				mailProps.put("mail.smtp.auth", authReq);
				session = Session.getInstance(mailProps, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, emailPwd);
					}
				});
			}else {
				mailProps.put("mail.smtp.auth", "false");
				session = Session.getInstance(mailProps);
			}

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notificaiton.getToEmail()));
			if(notificaiton.getCcEmail() != null)
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(notificaiton.getCcEmail()));
			message.setSubject(notificaiton.getSubject());
			message.setContent(notificaiton.getBody(), "text/html; charset=utf-8");

			Transport.send(message);
			log.info("Exit from sendEmail method");
		} catch (MessagingException me) {
			log.error("Unable to sendEmail method",me);
			throw new NotificationException(me.getMessage());
		}
	}
}
