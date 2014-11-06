package com.trailblazers.freewheelers.model;

import java.util.HashMap;
import java.util.Map;

public class AccountValidation {
    private static final String PASSWORD_FORMAT = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-/_<.>|='~`]).{8,20}$";
    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9](([\\.\\_\\-\\+]){0,1}[a-zA-Z\\d])*\\@[a-zA-Z\\d]([\\-]{0,1}[a-zA-Z\\d]|[a-zA-Z\\d]){0,254}(\\.[a-zA-Z]{2,3}){1,2}$";
    private static final String STREET1_FORMAT = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{1,255}$";
    private static final String STREET2_FORMAT = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{0,255}$";
    private static final String CITY_FORMAT = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{1,100}$";
    private static final String STATE_FORMAT = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{0,100}$";
    private static final String PHONE_NUMBER_FORMAT = "[\\+(0-9)\\-]*";
    private HashMap<String, String> errors = new HashMap<String, String>();
    private Account account;

    public Map<String, String> verifyInputs(Account account) {
        this.account = account;
        validateEmail();
        validateName();
        validateCountry();
        validateStreet1();
        validateStreet2();
        validateCity();
        validateState();
        validatePostcode();
        validatePhoneNumber();
        validatePassword();
        return errors;
    }

    private void validatePassword() {
        if (!account.getPassword().matches(PASSWORD_FORMAT)){
            errors.put("password", "Must meet password requirement!");
        }
    }

    private void validatePostcode() {
        if(!account.getPostcode().matches("^[0-9a-zA-Z\\-]{4,10}$")) {
            errors.put("postcode","Must enter a post code!");
        }
    }

    private void validateState() {
        if(!account.getState_Province().matches(STATE_FORMAT)) {
            errors.put("stateProvince","Must enter a valid state or province!");
        }
    }

    private void validateCity() {
        if(!account.getCity().matches(CITY_FORMAT)) {
            errors.put("city","Must enter a city!");
        }
    }

    private void validateStreet2() {
        if(!account.getStreet2().matches(STREET2_FORMAT)) {
            errors.put("street2","Enter a valid street!");
        }
    }

    private void validateStreet1() {
        if(!account.getStreet1().matches(STREET1_FORMAT)) {
            errors.put("street1", "Must enter a street!");
        }
    }

    private void validateCountry() {
        if(CountryValidation.verifyInputs(account.getCountry()).containsKey("country")) {
            errors.putAll(CountryValidation.verifyInputs(account.getCountry()));
        }
    }

    private void validateName() {
        if(account.getAccount_name().isEmpty()) {
            errors.put("name", "Must enter a name!");
        }
    }

    private void validateEmail() {
        if (!account.getEmail_address().matches(EMAIL_FORMAT)) {
           errors.put("email", "Must enter a valid email!");
        }
    }

    private void validatePhoneNumber() {
        boolean isPhoneNumberEmpty = account.getPhoneNumber().isEmpty();
        boolean doesPhoneNumberMatchRequirement = !account.getPhoneNumber().matches(PHONE_NUMBER_FORMAT);

        if(isPhoneNumberEmpty || doesPhoneNumberMatchRequirement) {
            errors.put("phoneNumber", "Must enter valid phone number (Not empty, and can only contain: numbers, plus, dash and parenthesis).");
        }
    }

}