package com.trailblazers.freewheelers.model;

import java.util.HashMap;

public class AccountValidation {
    private static final String passwordRequirement = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-/_<.>|='~`]).{8,20}$";
    private static final String emailFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String streetRequirements = "^[^-][a-zA-Z0-9-].{0,252}[^-]$";

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

        if(!account.getStreet1().matches(streetRequirements)) {
            errors.put("street1", "Must enter a street!");
        }

        if(!account.getStreet2().isEmpty() && !(account.getStreet2().matches(streetRequirements))) {
            errors.put("street2","Enter a valid street!");
        }

        if(!account.getCity().matches("^[^-][a-zA-Z0-9-].{0,97}[^-]$")) {
            errors.put("city","Must enter a city!");
        }

        if(account.getPostcode().isEmpty()) {
            errors.put("postcode","Must enter a post code!");
        }

        if(!account.getPostcode().matches("^[^-][0-9-].{0,7}[^-]$")) {
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