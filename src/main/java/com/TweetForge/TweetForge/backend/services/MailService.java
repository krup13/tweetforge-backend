package com.TweetForge.TweetForge.backend.services;

import com.TweetForge.TweetForge.backend.config.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.gmail.Gmail;

import javax.mail.MessagingException;

@Service
public class MailService {

    private final Gmail gmail;

    @Autowired
    public MailService(Gmail gmail) {
        this.gmail = gmail;
    }

//    public void sendEmail(String toAddress, String subject, String content) throws EmailFailedToSendException {
//        Properties props = new Properties();
//
//        Session session = Session.getInstance(props, null);
//
//        MimeMessage email = new MimeMessage(session);
//
//        try {
//            email.setFrom(new InternetAddress("mohammadnazrinmn0965@gmail.com"));
//            email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(toAddress));
//            email.setSubject(subject);
//            email.setText(content);
//
//            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//            email.writeTo(buffer);
//
//            byte[] rawMessageBytes = buffer.toByteArray();
//
//            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
//
//            Message message = new Message();
//            message.setRaw(encodedEmail);
//
//            message = gmail.users().messages().send("me", message).execute();
//        } catch (Exception e) {
//            throw new EmailFailedToSendException();
//        }
//
    
    public void sendEmail(String address, String subject, String content) throws MessagingException {
        Email email = new Email("mohammadnazrin.edu@gmail.com", "lkxtcixjaxqtippg");
        email.composeEmail(address, subject);
        email.composeBody(content);
        email.sendEmail();
    }

}
