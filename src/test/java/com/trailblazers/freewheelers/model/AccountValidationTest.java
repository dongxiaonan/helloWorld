package com.trailblazers.freewheelers.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                .setStreet2("")
                .setCity(SOME_CITY)
                .setState_Province("")
                .setPostcode(SOME_POSTCODE)
                .setEnabled(true);

    }
    @Test
    public void shouldHaveNoErrorsForValidInput() throws Exception {
        Map<String, String>  errors = verifyInputs(account);

        assertThat(errors.size(), is(0));
    }


    @Test
    public void shouldComplainAboutAnInvalidEmail() throws Exception {
        List<String> invalidEmails = new ArrayList<String>();
        invalidEmails.add("invalid.email.address");
        invalidEmails.add("inva|id@email.com");
        invalidEmails.add("invalid@emai|.com");
        invalidEmails.add("invalid__a@email.com");
        invalidEmails.add("invalid_@email.com");
        invalidEmails.add("invalid@email-.com");
        invalidEmails.add("invalid@-email.com");
        invalidEmails.add("invalid@email.c.o");
        invalidEmails.add("invalid@email.co.org.in");


        for(String invalidEmail :invalidEmails) {
            account.setEmail_address(invalidEmail);
            Map<String, String>  errors = verifyInputs(account);

            assertThereIsOneErrorFor("email", "enter a valid email", errors);
        }
    }

    @Test
    public void shouldNotComplainAboutAValidEmail() throws Exception {
        List<String> validEmails = new ArrayList<String>();
        validEmails.add(SOME_EMAIL);
        validEmails.add("valid@email.co.in");
        validEmails.add("valid1_c@email.com");
        validEmails.add("valid+a@email.com");
        validEmails.add("valid@e-mail.com");
        validEmails.add("valid@9mail9.com");
        validEmails.add("12valid@9mail9.com");

        for(String validEmail:validEmails) {
            account.setEmail_address(validEmail);
            Map<String, String>  errors = verifyInputs(account);

            assertThat(errors.size(),is(0));
        }
    }

    @Test
    public void shouldComplainAboutAnEmptyPassword() throws Exception {
        String emptyPassword = "";

        account.setPassword(emptyPassword);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }


    @Test
    public void shouldComplainAboutAnEmptyName() throws Exception {
        String emptyName = "";

        account.setAccount_name(emptyName);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("name", "enter a name", errors);
    }

    @Test
    public void shouldComplainAboutAnEmptyPhoneNumber() throws Exception {
        String emptyPhoneNumber = "";

        account.setPhoneNumber(emptyPhoneNumber);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("phoneNumber", "Must enter valid phone number (Not empty, and can only contain: numbers, plus, dash and parenthesis.)", errors);
    }

    @Test
    public void shouldComplainWhenPhoneNumberContainsCharacter() throws Exception {
        String invalidPhoneNumber = "a";
        account.setPhoneNumber(invalidPhoneNumber);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("phoneNumber", "Must enter valid phone number (Not empty, and can only contain: numbers, plus, dash and parenthesis.)", errors);
    }

    @Test
    public void shouldNotComplainWhenPhoneNumberContainsDash() throws Exception {
        String validPhoneNumberWithDash = "222-222";
        account.setPhoneNumber(validPhoneNumberWithDash);

        Map<String, String>  errors = verifyInputs(account);

        assertThat(errors.size(), is(0));
    }

    @Test
    public void shouldNotComplainWhenPhoneNumberContainsPlus() throws Exception {
        String validPhoneNumberWithPlus = "+0086222222";
        account.setPhoneNumber(validPhoneNumberWithPlus);

        Map<String, String>  errors = verifyInputs(account);

        assertThat(errors.size(), is(0));
    }

    @Test
    public void shouldNotComplainWhenPhoneNumberContainsParenthesis() throws Exception {
        String validPhoneNumberWithParenthesis = "(008)6222222";
        account.setPhoneNumber(validPhoneNumberWithParenthesis);

        Map<String, String> errors = verifyInputs(account);

        assertThat(errors.size(), is(0));
    }

    @Test
    public void shouldComplainWhenPhoneNumberContainsSpecialCharacters() throws Exception {
        String validPhoneNumberWithParenthesis = "&008&6222222";
        account.setPhoneNumber(validPhoneNumberWithParenthesis);

        Map<String, String> errors = verifyInputs(account);

        assertThereIsOneErrorFor("phoneNumber", "Must enter valid phone number (Not empty, and can only contain: numbers, plus, dash and parenthesis.)", errors);
    }

    @Test
    public void shouldComplainAboutAnEmptyCountry() throws Exception {
        account.setCountry(null);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("country","select a country",errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsNumber() throws Exception {
        String password = "!invalidPassword";
        account.setPassword(password);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsSpecialCapital() throws Exception {
        String password = "1invalidPassword";
        account.setPassword(password);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsUpperCase() throws Exception {
        String password = "!invalidpassword1";
        account.setPassword(password);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsLowerCase() throws Exception {
        String password = "!1CONFIRMEDPASSWORD";
        account.setPassword(password);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordLessThan8() throws Exception {
        String password = "a!2Pass";
        account.setPassword(password);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }

    @Test
    public void shouldComplainWhenPasswordNotContainsMoreThan20() throws Exception {
        String password = "!invalidPassword123qq";
        account.setPassword(password);

        Map<String, String>  errors = verifyInputs(account);

        assertThereIsOneErrorFor("password", "meet password requirement", errors);
    }
    private void assertThereIsOneErrorFor(String field, String expected, Map<String, String> errors) {
        assertThat(errors.size(), is(1));
        assertThat(errors.get(field), containsString(expected));

    }
}
