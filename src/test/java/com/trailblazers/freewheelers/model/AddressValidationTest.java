package com.trailblazers.freewheelers.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static com.trailblazers.freewheelers.model.AccountValidation.verifyInputs;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressValidationTest {


    public static final String SOME_EMAIL = "guenter.grass@gmail.com";
    public static final String SOME_PASSWORD = "V3ry Secure!";
    public static final String SOME_NAME = "Günter Grass";
    public static final String SOME_PHONE = "004945542741";
    public static final Country SOME_COUNTRY = new Country(1, "United Kingdom");
    public static final String SOME_STREET = "Greenwood Avenue";
    public static final String SOME_CITY = "London";
    public static final String SOME_POSTCODE = "12453";
    private Account account;

    @Before
    public void setup() {
        account = new Account()
                .setEmail_address(SOME_EMAIL)
                .setPassword(SOME_PASSWORD)
                .setAccount_name(SOME_NAME)
                .setCountry(SOME_COUNTRY)
                .setPhoneNumber(SOME_PHONE)
                .setStreet1(SOME_STREET)
                .setStreet2(SOME_STREET)
                .setCity(SOME_CITY)
                .setPostcode(SOME_POSTCODE)
                .setEnabled(true);

    }

    @Test
    public void shouldComplainAboutAnEmptyStreet1() throws Exception {
        String emptyStreet1 = "";
        account.setStreet1(emptyStreet1);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("street1", "enter a street", errors);
    }

    @Test
    public void shouldComplainWhenStreet1Beyond255() throws Exception {
        char[] chars = new char[256];
        Arrays.fill(chars, 'a');
        String street1 = new String(chars);
        account.setStreet1(street1);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("street1", "enter a street", errors);
    }

    @Test
    public void shouldComplainWhenStreet1ContainsSpecialCharacters() throws Exception {
        String street1 = "!@#";
        account.setStreet1(street1);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("street1", "enter a street", errors);
    }

    @Test
    public void shouldComplainWhenStreet2LengthIsBeyond255() throws Exception {
        char[] chars = new char[256];
        Arrays.fill(chars, 'a');
        String street2 = new String(chars);
        account.setStreet2(street2);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("street2", "Enter a valid street", errors);
    }

    @Test
    public void shouldComplainWhenStreet2ContainsSpecialCharacters() throws Exception {
        String street2 = "!@#";
        account.setStreet2(street2);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("street2", "Enter a valid street", errors);
    }

    @Test
    public void shouldComplainWhenCityContainsSpecialCharacter() throws Exception {
        String city = "aaa22@33aaa";

        account.setCity(city);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("city", "enter a city", errors);
    }

    @Test
    public void shouldComplainWhenCityLengthIsBeyond100() throws Exception {
        char[] chars = new char[101];
        Arrays.fill(chars, 'a');
        String city = new String(chars);
        account.setCity(city);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("city", "enter a city", errors);
    }

    @Test
    public void shouldComplainWhenCityIsEmpty() throws Exception {
        String city = "";
        account.setCity(city);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("city", "enter a city", errors);
    }


    @Test
    public void shouldComplainAboutAnEmptyPostcode() throws Exception {
        String emptyPostcode = "";

        account.setPostcode(emptyPostcode);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("postcode", "enter a post code", errors);
    }

    @Test
    public void shouldComplainWhenAPostcodeNotIn1to10() throws Exception {
        String postcode = "12345678901";

        account.setPostcode(postcode);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("postcode", "enter a post code", errors);
    }

    private void assertThereIsOneErrorFor(String field, String expected, HashMap<String, String> errors) {
        assertThat(errors.size(), is(1));
        assertThat(errors.get(field), containsString(expected));

    }
}
