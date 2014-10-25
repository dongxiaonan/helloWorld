package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.AccountValidation;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.CountryService;
import com.trailblazers.freewheelers.service.ServiceResult;
import com.trailblazers.freewheelers.service.impl.AccountServiceImpl;
import com.trailblazers.freewheelers.service.impl.CountryServiceImpl;
import org.apache.log4j.Logger;
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
    private Account account;
    private String confirmedPassword;

    public AccountController() {
        accountService = new AccountServiceImpl();
        countryService = new CountryServiceImpl();
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
        confirmedPassword = request.getParameter("confirmedPassword");

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
                .setEnabled(true);

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
}
