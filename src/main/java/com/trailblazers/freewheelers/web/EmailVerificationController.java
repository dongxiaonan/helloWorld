package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.EncryptionService;
import com.trailblazers.freewheelers.service.impl.AccountServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/emailverification")
public class EmailVerificationController {


    private AccountService accountService;

    private EncryptionService encryptionService;

    public EmailVerificationController()
    {
        accountService = new AccountServiceImpl();
        encryptionService = new EncryptionService();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(HttpServletRequest request) {
        String encryptedEmail =  request.getParameter("q");
        if(accountService.enabledAccountByEmail(encryptionService.getHexToString(encryptedEmail))) {
            return "emailverification";
        }
        return "account/createFailure";
    }

}
