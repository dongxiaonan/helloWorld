package com.trailblazers.freewheelers.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request,response);
        if(savedRequest.getRedirectUrl().contains("/shoppingCart/addToCart")) {
            this.setUseReferer(true);
            response.sendRedirect("/shoppingCart/addToCart");
        } else {
            this.setUseReferer(false);
            response.sendRedirect("/");
        }
    }
}
