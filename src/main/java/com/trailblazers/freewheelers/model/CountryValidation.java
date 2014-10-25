package com.trailblazers.freewheelers.model;

import java.util.HashMap;

public class CountryValidation {


    public static HashMap<String, String> verifyInputs(Country country) {
        HashMap<String,String> errors= new HashMap<String, String>();
        if(!isCountryValid(country)) {
            errors.put("country", "Must select a country!");
        }
        return errors;
    }

    private static boolean isCountryValid(Country country) {
        return country!=null && country.getName()!=null && country.getCountryId()!=null &&
                (country.getCountryId()!=0 && !country.getName().equals(""));
    }
}
