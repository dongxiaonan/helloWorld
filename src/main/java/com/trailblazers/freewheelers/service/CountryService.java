package com.trailblazers.freewheelers.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountryService {

    List<String> getCountries();

}
