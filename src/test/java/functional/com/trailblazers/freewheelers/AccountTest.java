package functional.com.trailblazers.freewheelers;

import org.junit.Test;

import static functional.com.trailblazers.freewheelers.helpers.SyntaxSugar.*;
import static org.junit.Assert.assertEquals;

public class AccountTest extends UserJourneyBase {

    @Test
    public void testUserProfileJourney() throws Exception {
        String jan = "Jan Plewka";
        String userAccount = "Hugo Huser";
        String Arno = "Arno Admin";

        admin
                .there_is_no_account_for(jan)
                .there_is_a_user(userAccount, SOME_PASSWORD)
                .there_is_an_admin(Arno, SOME_PASSWORD);

        user
                .is_logged_out()
                .logs_in_with(jan, SOME_PASSWORD);

        screen
                .shows_error_alert("login attempt was not successful");

        user
                .creates_an_account(jan, SOME_EMAIL, SOME_PASSWORD, SOME_STREET, SOME_STREET, SOME_CITY, SOME_POSTCODE, SOME_STATE, EMPTY_COUNTRY, EMPTY_PASSWORD, SOME_CONFIRMED_PASSWORD);

        screen
                .shows_error_alert("There were errors");

        user
                .creates_an_account(jan, SOME_EMAIL, SOME_PASSWORD, SOME_STREET, SOME_STREET, SOME_CITY, SOME_POSTCODE, SOME_STATE, VALID_COUNTRY, SOME_PHONE_NUMBER, SOME_INVALID_CONFIRMEDPASSWORD);

        screen
                .shows_error_alert("There were errors");

        user
                .creates_an_account(jan, SOME_EMAIL, SOME_PASSWORD, SOME_STREET, SOME_STREET, SOME_CITY, SOME_POSTCODE, SOME_STATE, VALID_COUNTRY, SOME_PHONE_NUMBER, SOME_CONFIRMED_PASSWORD);

        screen
                .shows_message("new account has been created");

        user
                .creates_an_account(jan, SOME_EMAIL, SOME_PASSWORD, SOME_STREET, SOME_STREET, SOME_CITY, SOME_POSTCODE, SOME_STATE, VALID_COUNTRY, SOME_PHONE_NUMBER, SOME_CONFIRMED_PASSWORD);

        screen
                .shows_error("Email address is already being used");

        user
                .is_logged_out()
                .logs_in_with(jan, SOME_PASSWORD);

        screen
                .shows_error_alert("Your login attempt was not successful, try again.");

        user
                .verifies_email_wih_link(VERIFICATION_ID);

        screen
                .shows_message("Thank you for verifying your email address!");

        user
                .logs_in_with(jan, SOME_PASSWORD);

        screen
                .shouldGoToHomePage()
                .should_not_contain_nps_report_link_in_header();

        user
                .tries_to_view_profile_of(userAccount);

        screen
                .shows_error_alert("access is denied");
        user
                .tries_to_view_profile_of(Arno);
        screen
                .shows_error_alert("access is denied");
        user
                .visits_his_profile();
        screen
                .should_show_phone_number()
                .shouldShowAddressOfTheUser();
    }

    @Test
    public void testAdminProfileJourney() throws Exception {
        String Hugo = "Hugo Huser";
        String Arno = "Arno Admin";

        String item = "Test ITEM";

        admin
                .there_is_a_user(Hugo, SOME_PASSWORD)
                .there_is_an_admin(Arno, SOME_PASSWORD)
                .there_is_a_frame(item, 10l);

        user
                .is_logged_out()
                .visits_his_profile();

        screen
                .shows_login();

        user
                .logs_in_with(Arno, SOME_PASSWORD)
                .visits_admin_profile();

        screen
                .shows_admin_profile();

        user
                .visits_profile_for(Hugo);

        screen
                .shows_profile_for(Hugo)
                .should_show_phone_number()
                .shouldShowAddressOfTheUser();
        user
                .visits_nps_report_page();
        screen
                .shouldShowNPSReportPage();
    }

    @Test
    public void shouldReturnPasswordWhenAskedForTypePasswordField() {
        user
                .is_logged_out();
        assertEquals("password", user.verify_password_field_is_masked_in_create_account());
        assertEquals("password", user.verify_confirmedPassword_field_is_masked_in_create_account());
        assertEquals("password", user.verify_password_field_is_masked_in_login());
    }

}
