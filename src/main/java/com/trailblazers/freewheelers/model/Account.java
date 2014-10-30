package com.trailblazers.freewheelers.model;

public class Account {

    private Long account_id;
    private String account_name;
    private String password;
    private boolean enabled;
    private String emailAddress;
    private String phoneNumber;
    private Country country;
    private String street1;
    private String street2;
    private String city;
    private String stateProvince;
    private String postcode;

    public Account() {
        this.account_id = 0L;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public Account setAccount_name(String account_name) {
        this.account_name = account_name;
        return this;
    }

    public Account setAccount_id(Long account_id) {
        this.account_id = account_id;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Account setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Account setEmail_address(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getEmail_address() {
        return emailAddress;
    }

    public Account setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Account setStreet1(String street1) {
        this.street1 = street1;
        return this;
    }

    public String getStreet1() {
        return street1;
    }

    public Account setStreet2(String street2) {
        this.street2 = street2;
        return this;
    }

    public String getStreet2() {
        return street2;
    }

    public Account setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCity() {
        return city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Country getCountry() {
        return country;
    }

    public Account setCountry(Country country) {
        this.country = country;
        return this;
    }

    public Account setState_Province(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public String getState_Province() {
        return stateProvince;
    }

    public Account setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (enabled != account.enabled) return false;
        if (account_id != null ? !account_id.equals(account.account_id) : account.account_id != null) return false;
        if (account_name != null ? !account_name.equals(account.account_name) : account.account_name != null)
            return false;
        if (city != null ? !city.equals(account.city) : account.city != null) return false;
        if (country != null ? !country.equals(account.country) : account.country != null) return false;
        if (emailAddress != null ? !emailAddress.equals(account.emailAddress) : account.emailAddress != null)
            return false;
        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(account.phoneNumber) : account.phoneNumber != null) return false;
        if (postcode != null ? !postcode.equals(account.postcode) : account.postcode != null) return false;
        if (stateProvince != null ? !stateProvince.equals(account.stateProvince) : account.stateProvince != null)
            return false;
        if (street1 != null ? !street1.equals(account.street1) : account.street1 != null) return false;
        if (street2 != null ? !street2.equals(account.street2) : account.street2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = account_id != null ? account_id.hashCode() : 0;
        result = 31 * result + (account_name != null ? account_name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (street1 != null ? street1.hashCode() : 0);
        result = 31 * result + (street2 != null ? street2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (stateProvince != null ? stateProvince.hashCode() : 0);
        result = 31 * result + (postcode != null ? postcode.hashCode() : 0);
        return result;
    }

    public String getAddress() {
        if(country == null) return "";
        String anAddress = street1;
        anAddress = anAddress.concat(street2.isEmpty()? "":"\n" + street2);
        anAddress = anAddress.concat("\n" + city + ", " + (stateProvince.isEmpty()?"": stateProvince + ", " )+ postcode);
        return anAddress.concat("\n" + country.getName());
    }
}
