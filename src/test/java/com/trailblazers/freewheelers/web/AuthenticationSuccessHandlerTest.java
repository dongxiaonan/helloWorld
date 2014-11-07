package com.trailblazers.freewheelers.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationSuccessHandlerTest {

    @Mock
    private SavedRequest savedRequest;

    @Mock
    private HttpSessionRequestCache requestCache;

    @InjectMocks
    private AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandler();

    @Test
    public void shouldRedirectToShoppingCartWhenClickedOnAddToCart() throws Exception {
        when(requestCache.getRequest(any(HttpServletRequest.class),any(HttpServletResponse.class))).thenReturn(savedRequest);
        when(savedRequest.getRedirectUrl()).thenReturn("/shoppingCart/addToCart");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authenticationSuccessHandler.onAuthenticationSuccess(request,response,mock(Authentication.class));

        verify(response).sendRedirect("/shoppingCart/addToCart");
    }

    @Test
    public void shouldRedirectToHomeWhenCalledByAdminOrUserProfile() throws Exception {
        when(requestCache.getRequest(any(HttpServletRequest.class),any(HttpServletResponse.class))).thenReturn(savedRequest);
        when(savedRequest.getRedirectUrl()).thenReturn("/userProfile");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authenticationSuccessHandler.onAuthenticationSuccess(request,response,mock(Authentication.class));

        verify(response).sendRedirect("/");
    }
}