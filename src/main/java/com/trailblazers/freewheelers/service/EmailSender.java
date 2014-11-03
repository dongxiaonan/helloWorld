package com.trailblazers.freewheelers.service;

import com.trailblazers.freewheelers.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private EmailService emailService;
    @Autowired
    private EncryptionService encryptionService;

    public void sendVerificationEmail(String serverURL, Account account) {
        String verificationID = encryptionService.getStringToHex(account.getEmail_address());

        String subject = "FreeWheelers Account Verification";
        StringBuilder text = new StringBuilder();
        text.append("<html><header></header><body>");
        text.append("Dear ").append(account.getAccount_name()).append(",<br><br>");
        text.append("This is an auto-generated email to verify your email address. Please do not reply to this email.<br>");
        text.append("To verify your account please click the link below:<br><br>");
        text.append("<a href=\"http://").append(serverURL).append("/emailverification?q=");
        text.append(verificationID);
        text.append("\">Click here to verify your email address</a>");
        text.append("<br><br>Yours,<br>FreeWheelers Team!<br><br>");
        text.append("<a href=\"http://").append(serverURL).append("\" style=\"text-decoration:none;\">");
        text.append("<img width = \"30px\" src=\"cid:image\" alt=\"FreeWheeler Logo\"><h1 style=\"display:inline; font-size:35px; color:#1485E5;\">FreeWheelers</h1></a>");
        text.append("</body></html>");
        emailService.send(account.getAccount_name(), account.getEmail_address(), text.toString(), subject);
    }
}
