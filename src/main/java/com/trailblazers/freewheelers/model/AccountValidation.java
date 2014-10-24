package com.trailblazers.freewheelers.model;

import java.util.HashMap;

public class AccountValidation {
    private static final String passwordRequirement = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-/_<.>|='~`]).{8,20}$";

    public static HashMap verifyInputs(Account account) {
        HashMap errors = new HashMap();

        if (!account.getEmail_address().contains("@")) {
           errors.put("email", "Must enter a valid email!");
        }


        if(account.getAccount_name().isEmpty()) {
            errors.put("name", "Must enter a name!");
        }

        if(CountryValidation.verifyInputs(account.getCountry()).containsKey("country")) {
            errors.putAll(CountryValidation.verifyInputs(account.getCountry()));
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