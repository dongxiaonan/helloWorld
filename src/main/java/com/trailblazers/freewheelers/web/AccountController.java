package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.CountryService;
import com.trailblazers.freewheelers.service.ServiceResult;
import com.trailblazers.freewheelers.service.impl.AccountServiceImpl;
import com.trailblazers.freewheelers.service.impl.CountryServiceImpl;
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

    AccountService accountService;
    CountryService countryService;
    private Account account;

    public AccountController() {
        accountService = new AccountServiceImpl();
        countryService = new CountryServiceImpl();
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public ModelAndView createAccountForm(Model model) {
        account = account!=null?account:new Account()
                .setEmail_address("")
                .setPassword("")
                .setAccount_name("")
                .setCountry("")
                .setPhoneNumber("")
                .setEnabled(false);
        model.addAttribute("validationMessage",new ExtendedModelMap());
        model.addAttribute("countries",countryService.getCountries());
        model.addAttribute("account",account);
        return new ModelAndView("account/create", (Map<String, ?>) model);
    }

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ModelAndView processCreate(HttpServletRequest request) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        String phoneNumber = request.getParameter("phoneNumber");

        account = (account!=null?account:new Account())
                .setEmail_address(email)
                .setPassword(password)
                .setAccount_name(name)
                .setCountry(country)
                .setPhoneNumber(phoneNumber)
                .setEnabled(true);

        try {
            ServiceResult<Account> result = accountService.createAccount(account);

            if (result.hasErrors()) {
                return showErrors(result.getErrors());
            }
            return showSuccess(result.getModel());
        } catch (Exception e) {
            return showError();
        }
    }

    private ModelAndView showErrors(Map errors) {
        Map<String,Object> model = new HashMap<String, Object>();
        model.put("countries",countryService.getCountries());
        ModelMap modelMap = new ModelMap();
        modelMap.put("errors", errors);
        if(errors.containsKey("email")){
            account.setEmail_address("");
        }
        if(errors.containsKey("password")){
            account.setPassword("");
        }
        if(errors.containsKey("name")){
            account.setAccount_name("");
        }
        if(errors.containsKey("country")){
            account.setCountry("");
        }
        if(errors.containsKey("phoneNumber")){
            account.setPhoneNumber("");
        }
        model.put("validationMessage",modelMap);
        model.put("account",account);
        return new ModelAndView("account/create", model);
    }

    private ModelAndView showError() {
        return new ModelAndView("account/createFailure");
    }

    private ModelAndView showSuccess(Account account) {
        ModelMap model = new ModelMap();
        model.put("name", account.getAccount_name());
        account = account
                .setEmail_address("")
                .setPassword("")
                .setAccount_name("")
                .setCountry("")
                .setPhoneNumber("")
                .setEnabled(false);
        return new ModelAndView("account/createSuccess", "postedValues", model);
    }

}
