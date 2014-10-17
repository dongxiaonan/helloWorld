package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.service.CountryService;

import java.util.ArrayList;
import java.util.List;

public class CountryServiceImpl implements CountryService{

    @Override
    public List<String> getCountries() {
        List<String> list=new ArrayList<String>();
        list.add("UK");
        list.add("USA");
        list.add("France");
        list.add("Germany");
        list.add("Canada");
        list.add("Italy");

        return list;
    }
}
