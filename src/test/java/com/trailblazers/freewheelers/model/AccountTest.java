package com.trailblazers.freewheelers.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AccountTest {

    @Test
    public void testCreatingNewAccounts() throws Exception {
        Account account = new Account()
                .setAccount_name("Bob")
                .setPassword("password")
                .setEmail_address("foo@bar.com")
                .setCountry(new Country(1,"United Kingdom"))
                .setPhoneNumber("123443245");

        assertThat(account.getAccount_name(), is("Bob"));
        assertThat(account.getPassword(), is("password"));
        assertThat(account.getEmail_address(), is("foo@bar.com"));
        assertThat(account.getCountry(), is(new Country(1,"United Kingdom")));
        assertThat(account.getPhoneNumber(), is("123443245"));
    }
}
