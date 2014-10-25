package com.trailblazers.freewheelers.model;

public class Country {


    private final Integer countryId;
    private final String name;

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

        return !(countryId != null ? !countryId.equals(country.countryId) : country.countryId != null) && !(name != null ? !name.equals(country.name) : country.name != null);

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
