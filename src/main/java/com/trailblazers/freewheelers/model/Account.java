package com.trailblazers.freewheelers.model;

public class Account {

    private Long account_id;
    private String account_name;
    private String password;
    private boolean enabled;
    private String emailAddress;
    private String phoneNumber;
    private String country;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public Account setCountry(String country) {
        this.country = country;
        return this;
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
        if (country != null ? !country.equals(account.country) : account.country != null) return false;
        if (emailAddress != null ? !emailAddress.equals(account.emailAddress) : account.emailAddress != null)
            return false;
        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(account.phoneNumber) : account.phoneNumber != null) return false;

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
        return result;
    }
}
