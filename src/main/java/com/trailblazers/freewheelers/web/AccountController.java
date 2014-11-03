package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountValidation;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.CountryService;
import com.trailblazers.freewheelers.service.EmailSender;
import com.trailblazers.freewheelers.service.ServiceResult;
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

    @Autowired
    private AccountService accountService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private EmailSender emailSender;

    public AccountController() {
        this.logger = Logger.getLogger(AccountController.class);
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
        String confirmedPassword = request.getParameter("confirmedPassword");
        String serverURL = request.getServerName() + ":" + request.getServerPort();

        Account account = new Account()
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

            if(!isPasswordMatch(account.getPassword(), confirmedPassword)){
                Map errors = new HashMap();
                errors.put("confirmedPassword", "Must have matching password!");
                errors.putAll(AccountValidation.verifyInputs(account));
                return showErrors(errors);
            }

            ServiceResult<Account> result = accountService.createAccount(account);

            if (result.hasErrors()) {
                return showErrors(result.getErrors());
            }

            emailSender.sendVerificationEmail(serverURL, account);

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


    private boolean isPasswordMatch(String password, String confirmedPassword) {
        return confirmedPassword != null && confirmedPassword.equals(password);
    }

}
