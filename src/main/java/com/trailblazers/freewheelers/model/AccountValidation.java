package com.trailblazers.freewheelers.model;

import java.util.HashMap;
import java.util.Map;

public class AccountValidation {
    private static final String passwordRequirement = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-/_<.>|='~`]).{8,20}$";
    private static final String emailFormat = "^[a-zA-Z0-9](([\\.\\_\\-\\+]){0,1}[a-zA-Z\\d])*\\@[a-zA-Z\\d]([\\-]{0,1}[a-zA-Z\\d]|[a-zA-Z\\d]){0,254}(\\.[a-zA-Z]{2,3}){1,2}$";
    public static final String street1Requirement = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{1,255}$";
    public static final String street2Requirement = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{0,255}$";
    public static final String cityRequirment = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{1,100}$";
    public static final String stateProvinceRequirement = "^[0-9a-zA-Z\\-\\'\\ \\.\\,]{0,100}$";
    public static final String PHONE_NUMBER_REQUIREMENT = "[\\+(0-9)?\\-]*";

    public static Map<String, String> verifyInputs(Account account) {
        HashMap<String, String> errors = new HashMap<String, String>();

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

        if(!account.getPostcode().matches("^[0-9a-zA-Z\\-]{4,10}$")) {
            errors.put("postcode","Must enter a post code!");
        }

        validatePhoneNumber(account, errors);

        if (!account.getPassword().matches(passwordRequirement)){
            errors.put("password", "Must meet password requirement!");
        }
        return errors;
    }

    private static void validatePhoneNumber(Account account, HashMap<String, String> errors) {
        boolean isPhoneNumberEmpty = account.getPhoneNumber().isEmpty();
        boolean doesPhoneNumberMatchRequirement = !account.getPhoneNumber().matches(PHONE_NUMBER_REQUIREMENT);

        if(isPhoneNumberEmpty || doesPhoneNumberMatchRequirement) {
            errors.put("phoneNumber", "Must enter valid phone number (Not empty, and containing only numbers, plus, dash and parenthesis.");
        }
    }

}