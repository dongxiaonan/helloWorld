package com.trailblazers.freewheelers.web;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmailControllerTest {
    @Test
    public void shouldNotInvokeTransportSendMethod() throws Exception {
        EmailController expectedEmailController = new EmailController();
        assertFalse(expectedEmailController.send(null, null, null, null));
    }

    @Test
    public void shouldInvokeTransportSendMethod() throws Exception {
        EmailController expectedEmailController = new EmailController();
        String subject = "Account Validation Email";
        String userName = "testName";
        String userEmail = "test@testtest.com";
        String message = "please validate your account";

        assertTrue(expectedEmailController.send(userName, userEmail, message, subject));
    }
}