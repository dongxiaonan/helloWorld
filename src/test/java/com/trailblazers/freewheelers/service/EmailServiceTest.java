package com.trailblazers.freewheelers.service;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmailServiceTest {
    @Test
    public void shouldNotInvokeTransportSendMethod() throws Exception {
        EmailService expectedEmailService = new EmailService();
        assertFalse(expectedEmailService.send(null, null, null, null));
    }

    @Test
    public void shouldInvokeTransportSendMethod() throws Exception {
        EmailService expectedEmailService = new EmailService();
        String subject = "Account Validation Email";
        String userName = "testName";
        String userEmail = "test@testtest.com";
        String message = "please validate your account";

        assertTrue(expectedEmailService.send(userName, userEmail, message, subject));
    }
}