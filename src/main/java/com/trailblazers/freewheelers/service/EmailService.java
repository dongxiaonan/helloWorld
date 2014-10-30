package com.trailblazers.freewheelers.service;

import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService {

    public static final Logger logger = Logger.getLogger(EmailService.class.getName());

    public boolean send(String userName, String userEmail, String message, String subject) {
        if (userName == null || userEmail == null || message == null || subject == null) {
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "chidmzlnmta01.thoughtworks.com");
        props.put("mail.smtp.port", 25);
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = createMessage(session, userEmail, userName, message, subject);
            Transport.send(msg);
        } catch (Exception e) {
            logger.log(Level.INFO, "Caught Exception while sending an email!", e);
            return false;
        }

        return true;
    }

    private Message createMessage(Session session, String userEmail, String userName, String message, String subject) throws MessagingException, UnsupportedEncodingException {
        Message mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress("no-reply@freewheelers.com", "FreeWheelers Team"));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail, userName));
        mimeMessage.setSubject(subject);

        MimeMultipart messageParts = new MimeMultipart("related");

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message,"text/html");

        messageParts.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataSource fileDataSource = new FileDataSource("src/main/webapp/images/logo.png");
        messageBodyPart.setDataHandler(new DataHandler(fileDataSource));
        messageBodyPart.setHeader("Content-ID","<image>");

        messageParts.addBodyPart(messageBodyPart);

        mimeMessage.setContent(messageParts);

        return mimeMessage;
    }
}
