package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountValidation;
import com.trailblazers.freewheelers.service.*;
import com.trailblazers.freewheelers.service.impl.AccountServiceImpl;
import com.trailblazers.freewheelers.service.impl.CountryServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {
    private Logger logger;

    AccountService accountService;
    CountryService countryService;
    EncryptionService encryptionService;
    private Account account;
    private String confirmedPassword;

    @Autowired
    protected EmailService emailService;

    public AccountController() {
        accountService = new AccountServiceImpl();
        countryService = new CountryServiceImpl();
        this.logger = Logger.getLogger(AccountController.class);
        encryptionService = new EncryptionService();
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public ModelAndView createAccountForm(Model model) {
        model.addAttribute("validationMessage",new ExtendedModelMap());
        model.addAttribute("countries",countryService.getAllCountries());
        model.addAttribute("confirmedPassword","");
        return new ModelAndView("account/create", (Map<String, ?>) model);
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ModelAndView processCreate(HttpServletRequest request) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String street1 = request.getParameter("street1");
        String street2 = request.getParameter("street2");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String stateProvince = request.getParameter("stateProvince");
        String postcode = request.getParameter("postcode");
        String phoneNumber = request.getParameter("phoneNumber");
        confirmedPassword = request.getParameter("confirmedPassword");
        String serverURL = request.getServerName() + ":" + request.getServerPort();

        account = new Account()
                .setEmail_address(email)
                .setPassword(password)
                .setAccount_name(name)
                .setCountry(countryService.getCountryByName(country))
                .setStreet1(street1)
                .setStreet2(street2)
                .setCity(city)
                .setState_Province(stateProvince)
                .setPostcode(postcode)
                .setPhoneNumber(phoneNumber)
                .setEnabled(false);

        try {

            if(!isPasswordMatch()){
                Map errors = new HashMap();
                errors.put("confirmedPassword", "Must have matching password!");
                errors.putAll(AccountValidation.verifyInputs(account));
                return showErrors(errors);
            }

            ServiceResult<Account> result = accountService.createAccount(account);

            if (result.hasErrors()) {
                return showErrors(result.getErrors());
            }

            sendVerificationEmail(serverURL);

            return showSuccess(result.getModel());
        } catch (Exception e) {
            logger.error("Failed to create Account", e);
            return showError();
        }
    }

    private ModelAndView showErrors(Map errors) {
        Map<String,Object> model = new HashMap<String, Object>();
        model.put("countries",countryService.getAllCountries());
        ModelMap modelMap = new ModelMap();
        modelMap.put("errors", errors);
        model.put("validationMessage",modelMap);
        return new ModelAndView("account/create", model);
    }

    private ModelAndView showError() {
        return new ModelAndView("account/createFailure");
    }

    private ModelAndView showSuccess(Account account) {
        ModelMap model = new ModelMap();
        model.put("name", account.getAccount_name());
        return new ModelAndView("account/createSuccess", "postedValues", model);
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public boolean isPasswordMatch() {
        return confirmedPassword != null && confirmedPassword.equals(account.getPassword());
    }

    private void sendVerificationEmail(String serverURL) {
        String verificationID = encryptionService.getStringToHex(account.getEmail_address());

        String subject = "FreeWheelers Account Verification";
        StringBuilder text = new StringBuilder();
        text.append("<html><header></header><body>");
        text.append("Dear ").append(account.getAccount_name()).append(",<br><br>");
        text.append("This is an auto-generated email to verify your email address. Please do not reply to this email.<br>");
        text.append("To verify your account please click the link below:<br><br>");
        text.append("<a href=\"http://").append(serverURL).append("/emailverification?q.=");
        text.append(verificationID);
        text.append("\">Click here to verify your email address</a>");
        text.append("<br><br>Yours,<br>FreeWheelers Team!<br><br>");
        text.append("<a href=\"http://").append(serverURL).append("\" style=\"text-decoration:none;\">");
        text.append("<img width = \"30px\" src=\"cid:image\" alt=\"FreeWheeler Logo\"><h1 style=\"display:inline; font-size:35px; color:#1485E5;\">FreeWheelers</h1></a>");
        text.append("</body></html>");
        emailService.send(account.getAccount_name(), account.getEmail_address(), text.toString(), subject);
    }

}
