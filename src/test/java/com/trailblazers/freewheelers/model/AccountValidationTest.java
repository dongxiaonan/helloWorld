package com.trailblazers.freewheelers.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.trailblazers.freewheelers.model.AccountValidation.verifyInputs;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AccountValidationTest {

    public static final String SOME_EMAIL = "guenter.grass@gmail.com";
    public static final String SOME_PASSWORD = "V3ry Secure!";
    public static final String SOME_NAME = "Günter Grass";
    public static final String SOME_PHONE = "004945542741";
    public static final Country SOME_COUNTRY = new Country(1,"United Kingdom");
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
                .setCity(SOME_CITY)
                .setPostcode(SOME_POSTCODE)
                .setEnabled(true);

    }
    @Test
    public void shouldHaveNoErrorsForValidInput() throws Exception {
        HashMap errors = verifyInputs(account);

        assertThat(errors.size(), is(0));
    }
    
    @Test
    public void shouldComplainAboutAnInvalidEmail() throws Exception {
        String invalidEmail = "invalid.email.address";

        account.setEmail_address(invalidEmail);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("email", "enter a valid email", errors);
    }

    @Test
    public void shouldComplainAboutAnEmptyPassword() throws Exception {
        String emptyPassword = "";

        account.setPassword(emptyPassword);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainAboutAnEmptyStreet1() throws Exception {
        String emptyStreet1 = "";

        account.setStreet1(emptyStreet1);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("street1", "enter a street", errors);
    }

    @Test
    public void shouldComplainAboutAnEmptyCity() throws Exception {
        String emptyCity = "";

        account.setCity(emptyCity);

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
    public void shouldComplainAboutAnEmptyName() throws Exception {
        String emptyName = "";

        account.setAccount_name(emptyName);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("name", "enter a name", errors);
    }

    @Test
    public void shouldComplainAboutAnEmptyPhoneNumber() throws Exception {
        String emptyPhoneNumber = "";

        account.setPhoneNumber(emptyPhoneNumber);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("phoneNumber", "enter a phone number", errors);
    }

    @Test
    public void shouldComplainAboutAnEmptyCountry() throws Exception {
        account.setCountry(null);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("country","select a country",errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsNumber() throws Exception {
        String password = "!invalidPassword";
        account.setPassword(password);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsSpecialCapital() throws Exception {
        String password = "1invalidPassword";
        account.setPassword(password);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsUpperCase() throws Exception {
        String password = "!invalidpassword1";
        account.setPassword(password);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsLowerCase() throws Exception {
        String password = "!1CONFIRMEDPASSWORD";
        account.setPassword(password);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordLessThan8() throws Exception {
        String password = "!2Pass";
        account.setPassword(password);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsMoreThan20() throws Exception {
        String password = "!invalidPassword123qq";
        account.setPassword(password);

        HashMap errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }
    private void assertThereIsOneErrorFor(String field, String expected, HashMap<String, String> errors) {
        assertThat(errors.size(), is(1));
        assertThat(errors.get(field), containsString(expected));

    }


}
