package com.siwiak.java;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

	private final String username = "*****";
	private final String password = "*****";

	private final String messageSender = "*****";
	private String messageRecipient;
	private String messageSubject;
	private String messageContent;
	private Session session;

	public Mail() {

		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		this.session = session;

	}

	public void sendMessage() {
		
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(messageSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(messageRecipient));
			message.setSubject(messageSubject);
			message.setText(messageContent);
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

	public String getMessageRecipient() {
		return messageRecipient;
	}

	public void setMessageRecipient(String messageRecipient) {
		this.messageRecipient = messageRecipient;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

}
