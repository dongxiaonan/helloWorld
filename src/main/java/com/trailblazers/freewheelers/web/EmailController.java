package com.trailblazers.freewheelers.web;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailController {

    public static final Logger logger = Logger.getLogger(EmailController.class.getName());

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
            logger.log(Level.WARNING, "Caught Exception while sending an email!", e);
            return false;
        }

        return true;
    }

    private Message createMessage(Session session, String userEmail, String userName, String message, String subject) throws MessagingException, UnsupportedEncodingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("supportteam@freewheelers.com", "FreeWheelers Team")); // TODO: change the email address to a real one
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail, userName));
        msg.setSubject(subject);
        msg.setText(message);
        return msg;
    }
}
