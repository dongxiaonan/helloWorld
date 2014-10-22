package com.trailblazers.freewheelers.service;

import com.trailblazers.freewheelers.model.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {

    List<Country> getAllCountries();

    Country getCountryByName(String countryName);

    Country getCountryById(Integer countryID);
}
