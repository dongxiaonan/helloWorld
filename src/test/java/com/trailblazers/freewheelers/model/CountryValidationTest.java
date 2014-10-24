package com.trailblazers.freewheelers.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CountryValidationTest {

    private HashMap<String,String> error;


    @Before
    public void setUp() throws Exception {
        error = new HashMap<String, String>();
    }

    @Test
    public void shouldComplainAboutAnEmptyCountry() throws Exception {
        Country country = new Country(0, "");

        error=CountryValidation.verifyInputs(country);

        assertCountryErrors();
    }

    @Test
    public void shouldComplainAboutANullCountryName() throws Exception {
        Country country = new Country(0,null);

        error=CountryValidation.verifyInputs(country);

        assertCountryErrors();
    }

    @Test
    public void shouldComplainAboutANullCountryID() throws Exception {
        Country country = new Country(null, "");

        error=CountryValidation.verifyInputs(country);

        assertCountryErrors();
    }

    @Test
    public void shouldComplainAboutANullCountry() throws Exception {
        error=CountryValidation.verifyInputs(null);

        assertCountryErrors();
    }

    private void assertCountryErrors() {
        assertThat(error.size(), is(1));
        assertThat(error.get("country"),containsString("select a country"));
    }
}