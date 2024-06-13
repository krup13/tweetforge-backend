package com.TweetForge.TweetForge.backend.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class Email {
	private String senderEmail;
	
	private String senderPassword;
	private static final String EMAIL_HOST = "smtp.gmail.com";
	private final Session session = createSession(setUpTLSProperties());
	
	private final MimeMultipart body = new MimeMultipart("related");
	private final MimeMessage message = new MimeMessage(this.session);
	
	public Email(String senderEmail, String senderPassword) {
		this.senderEmail = senderEmail;
		this.senderPassword = senderPassword;
	}
	
	private Email() {
	
	}
	
	@NotNull
	private Properties setUpSSLProperties() {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.ssl.trust", EMAIL_HOST);
		prop.put("mail.smtp.host", EMAIL_HOST);
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.ssl.checkserveridentity", "true");
		
		return prop;
	}
	
	@NotNull
	private Properties setUpTLSProperties() {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.tls.trust", EMAIL_HOST);
		prop.put("mail.smtp.host", EMAIL_HOST);
		prop.put("mail.smtp.port", "587");
		
		return prop;
	}
	
	
	@NotNull
	@Contract("_ -> new")
	private Session createSession(Properties properties) {
		return Session.getInstance(
				properties,
				new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(senderEmail, senderPassword);
					}
				}
		);
	}
	
	public void composeEmail(String recipient, String subject) throws MessagingException {
		message.setSubject(subject);
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		
		message.saveChanges();
	}
	
	
	public void composeBody(String contentBody, String contentType, String charset) throws MessagingException {
		BodyPart messageBody = new MimeBodyPart();
		messageBody.setContent(contentBody, String.format("%s; charset=%s", contentType, charset));
		body.addBodyPart(messageBody);
	}
	
	public void composeBody(String contentBody) throws MessagingException {
		composeBody(contentBody, "text/plain", "utf-8");
	}
	
	
	public void composeAttachment(@NotNull ByteArrayOutputStream attachmentStream, String attachmentType, String attachmentFileName) throws MessagingException {
		BodyPart messageAttachment = new MimeBodyPart();
		
		byte[] bytes = attachmentStream.toByteArray();
		DataSource source = new ByteArrayDataSource(bytes, attachmentType);
		
		messageAttachment.setDataHandler(new DataHandler(source));
		messageAttachment.setFileName(attachmentFileName);
		
		body.addBodyPart(messageAttachment);
	}
	
	public void composeAttachment(String filePath, String attachmentFileName) throws MessagingException, IOException {
		MimeBodyPart messageAttachment = new MimeBodyPart();
		
		messageAttachment.attachFile(filePath);
		messageAttachment.setFileName(attachmentFileName);
		
		body.addBodyPart(messageAttachment);
	}
	
	public void addRecipients(String[] recipients, Message.RecipientType recipientType) throws MessagingException {
		message.addRecipients(
				recipientType,
				Arrays.stream(recipients)
						.map(recipient -> {
							try {
								return new InternetAddress(recipient);
							} catch (AddressException e) {
								throw new RuntimeException(e);
							}
						})
						.toArray(Address[]::new)
		);
		message.saveChanges();
	}
	
	public void sendEmail() throws MessagingException {
		message.setContent(body);
		message.saveChanges();
		
		Transport.send(message);
		
		System.out.println("Email sent successfully");
	}
}
