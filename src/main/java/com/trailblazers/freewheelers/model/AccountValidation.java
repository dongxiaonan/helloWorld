package com.trailblazers.freewheelers.model;

import java.util.HashMap;

public class AccountValidation {
    private static final String passwordRequirement = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-/_<.>|='~`]).{8,20}$";
    private static final String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final String street1Requirement = "^[0-9a-zA-Z\\-\\'\\ ]{1,255}$";
    public static final String street2Requirement = "^[0-9a-zA-Z\\-\\'\\ ]{0,255}$";
    public static final String cityRequirment = "^[0-9a-zA-Z\\-\\'\\ ]{1,100}$";
    public static final String stateProvinceRequirement = "^[0-9a-zA-Z\\-\\'\\ ]{0,100}$";

    public static HashMap verifyInputs(Account account) {
        HashMap errors = new HashMap();

        if (!account.getEmail_address().matches(emailFormat)) {
           errors.put("email", "Must enter a valid email!");
        }

        if(account.getAccount_name().isEmpty()) {
            errors.put("name", "Must enter a name!");
        }

        if(CountryValidation.verifyInputs(account.getCountry()).containsKey("country")) {
            errors.putAll(CountryValidation.verifyInputs(account.getCountry()));
        }

        if(!account.getStreet1().matches(street1Requirement)) {
            errors.put("street1", "Must enter a street!");
        }

        if(!account.getStreet2().matches(street2Requirement)) {
            errors.put("street2","Enter a valid street!");
        }

        if(!account.getCity().matches(cityRequirment)) {
            errors.put("city","Must enter a city!");
        }

        if(!account.getState_Province().matches(stateProvinceRequirement)) {
            errors.put("stateProvince","Must enter a valid state or province!");
        }

        if(!account.getPostcode().matches("^[0-9\\-]{4,10}$")) {
            errors.put("postcode","Must enter a post code!");
        }

        if(account.getPhoneNumber().isEmpty()) {
            errors.put("phoneNumber", "Must enter a phone number!");
        }

        if (!account.getPassword().matches(passwordRequirement)){
            errors.put("password", "Must meet password requirement!");
        }
        return errors;
    }

}