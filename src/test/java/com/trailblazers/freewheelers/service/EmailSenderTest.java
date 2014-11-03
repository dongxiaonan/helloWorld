package com.trailblazers.freewheelers.service;

import com.trailblazers.freewheelers.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailSenderTest {

    @Mock
    private EmailService emailService;
    @Mock
    private EncryptionService encryptionService;
    @InjectMocks
    private EmailSender emailSender = new EmailSender();

    @Test
    public void shouldCallEmailServiceWithTheRightEmailDetails() {
        Account boxing = new Account().setAccount_name("Boxing").setEmail_address("boxing@gmail.com");
        when(encryptionService.getStringToHex("boxing@gmail.com")).thenReturn("123");
        String emailMessage = "<html><header></header><body>Dear Boxing,<br><br>This is an auto-generated email to verify your email address. Please do not reply to this email.<br>To verify your account please click the link below:<br><br><a href=\"http://localhost:8080/emailverification?q=123\">Click here to verify your email address</a><br><br>Yours,<br>FreeWheelers Team!<br><br><a href=\"http://localhost:8080\" style=\"text-decoration:none;\"><img width = \"30px\" src=\"cid:image\" alt=\"FreeWheeler Logo\"><h1 style=\"display:inline; font-size:35px; color:#1485E5;\">FreeWheelers</h1></a></body></html>";

        emailSender.sendVerificationEmail("localhost:8080", boxing);

        verify(emailService).send("Boxing", "boxing@gmail.com", emailMessage, "FreeWheelers Account Verification");
    }

}