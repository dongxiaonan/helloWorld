package com.trailblazers.freewheelers.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        assertThat(error.size(), is(1));
        assertThat(error.get("country"),containsString("select a country"));
    }

    @Test
    public void shouldComplainAboutANullCountryName() throws Exception {
        Country country = new Country(0,null);

        error=CountryValidation.verifyInputs(country);

        assertThat(error.size(), is(1));
        assertThat(error.get("country"),containsString("select a country"));
    }

    @Test
    public void shouldComplainAboutANullCountryID() throws Exception {
        Country country = new Country(null, "");

        error=CountryValidation.verifyInputs(country);

        assertThat(error.size(), is(1));
        assertThat(error.get("country"),containsString("select a country"));
    }

    @Test
    public void shouldComplainAboutANullCountry() throws Exception {
        error=CountryValidation.verifyInputs(null);

        assertThat(error.size(), is(1));
        assertThat(error.get("country"),containsString("select a country"));
    }
}