package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.CountryMapper;
import com.trailblazers.freewheelers.model.Country;
import com.trailblazers.freewheelers.service.CountryService;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class CountryServiceImplTest {


    @Mock
    SqlSession sqlSession;
    @Mock
    CountryMapper countryMapper;

    @InjectMocks
    private CountryService countryService = new CountryServiceImpl();

    @Before
    public void setUp() throws Exception {
        when(sqlSession.getMapper(CountryMapper.class)).thenReturn(countryMapper);
    }

    @Test
    public void shouldReturnListOfCountriesAvailable() throws Exception {

        assertNotNull(countryService.getAllCountries());
    }

    @Test
    public void shouldReturnCountryWhenCountryNameIsGiven() throws Exception {
        when(countryMapper.getByName("United Kingdom")).thenReturn(new Country(1,"United Kingdom"));

        assertThat(countryService.getCountryByName("United Kingdom"), is(new Country(1, "United Kingdom")));
    }

    @Test
    public void shouldReturnCountryWhenCountryIdIsGiven() throws Exception {
        when(countryMapper.getById(1)).thenReturn(new Country(1,"United Kingdom"));

        assertThat(countryService.getCountryById(1), is(new Country(1, "United Kingdom")));
    }

    @Test
    public void shouldReturnCountriesInAlphabeticalOrder() throws Exception {
        List<Country> expectedOrderedCountries = new ArrayList<Country>();
        expectedOrderedCountries.add(new Country(1, "Canada"));
        expectedOrderedCountries.add(new Country(4, "France"));
        expectedOrderedCountries.add(new Country(3, "Germany"));
        expectedOrderedCountries.add(new Country(2, "United Kingdom"));
        when(countryMapper.findAll()).thenReturn(expectedOrderedCountries);

        List<Country> allCountries = countryService.getAllCountries();

        assertThat(allCountries, is(expectedOrderedCountries));
    }

}