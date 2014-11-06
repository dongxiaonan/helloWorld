package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.EncryptionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailVerificationControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private EmailVerificationController emailVerificationController = new EmailVerificationController();

    @Test
    public void shouldRedrictToSuccessWhenEmailExist() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("q")).thenReturn("757365724066726565776865656c6572732e636F6D");
        when(encryptionService.getHexToString("757365724066726565776865656c6572732e636F6D")).thenReturn("user@freewheelers.com");
        when(accountService.enabledAccountByEmail("user@freewheelers.com")).thenReturn(true);

        assertThat(emailVerificationController.get(request), is("emailverification"));
    }

    @Test
    public void shouldRedrictToFailureWhenEmailNotExist() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("q")).thenReturn("757365724066726565776865656c6572732e636F6D");
        when(encryptionService.getHexToString("757365724066726565776865656c6572732e636F6D")).thenReturn("user@freewheelers.com");
        when(accountService.enabledAccountByEmail("user@freewheelers.com")).thenReturn(false);

        assertThat(emailVerificationController.get(request), is("account/createFailure"));
    }
}