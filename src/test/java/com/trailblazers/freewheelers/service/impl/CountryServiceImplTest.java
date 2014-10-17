package com.trailblazers.freewheelers.service.impl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CountryServiceImplTest {

    @Test
    public void shouldReturnAnEmptyListIfNoCountryIsAvailable() throws Exception {
        CountryServiceImpl countryServiceImpl= new CountryServiceImpl();

        assertNotNull(countryServiceImpl.getCountries());
    }

    @Test
    public void shouldReturnAListWithAllTheCountriesAvailable() throws Exception {
        List<String> expectedResult=new ArrayList<String>();
        expectedResult.add("UK");
        expectedResult.add("USA");
        expectedResult.add("France");
        expectedResult.add("Germany");
        expectedResult.add("Canada");
        expectedResult.add("Italy");

        CountryServiceImpl countryServiceImpl= new CountryServiceImpl();

        assertEquals(expectedResult,countryServiceImpl.getCountries());
    }

}