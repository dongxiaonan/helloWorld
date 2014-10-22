package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.CountryMapper;
import com.trailblazers.freewheelers.mappers.MyBatisUtil;
import com.trailblazers.freewheelers.model.Country;
import com.trailblazers.freewheelers.service.CountryService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService{

    private CountryMapper countryMapper;
    private List<Country> allCountries;

    public CountryServiceImpl() {
        this(MyBatisUtil.getSqlSessionFactory().openSession());
    }

    public CountryServiceImpl(SqlSession sqlSession) {
        this.countryMapper = sqlSession.getMapper(CountryMapper.class);
    }

    @Override
    public List<Country> getAllCountries() {
        allCountries = (allCountries==null?countryMapper.findAll():allCountries);
        return allCountries;
    }

    @Override
    public Country getCountryByName(String countryName) {
        return countryMapper.getByName(countryName);
    }

    @Override
    public Country getCountryById(Integer countryID) {
        return countryMapper.getById(countryID);
    }
}
