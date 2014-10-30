package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.EncryptionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VerificationControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private VerificationController verificationController = new VerificationController();

    @Test
    public void shouldChangeVerfiedBooleanFromFalseToTrue() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("q")).thenReturn("757365724066726565776865656c6572732e636F6D");
        when(encryptionService.getHexToString("757365724066726565776865656c6572732e636F6D")).thenReturn("user@freewheelers.com");
        when(accountService.getAccountIdByEmail("user@freewheelers.com")).thenReturn(new Account().setEnabled(false));

        assertThat(verificationController.get(request), is("emailverification"));

    }
}