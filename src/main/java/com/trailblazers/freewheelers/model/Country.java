package com.trailblazers.freewheelers.model;

import java.util.Comparator;

public class Country {


    private Integer countryId;
    private String name;

    public Country(Integer countryId,String name) {
        this.name=name;
        this.countryId =countryId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (countryId != null ? !countryId.equals(country.countryId) : country.countryId != null) return false;
        if (name != null ? !name.equals(country.name) : country.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = countryId != null ? countryId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public Integer getCountryId() {
        return countryId;
    }
}
