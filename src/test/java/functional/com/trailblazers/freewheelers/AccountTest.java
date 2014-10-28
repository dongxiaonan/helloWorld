package functional.com.trailblazers.freewheelers;

import org.junit.Test;

import static functional.com.trailblazers.freewheelers.helpers.SyntaxSugar.*;
import static org.junit.Assert.assertEquals;

public class AccountTest extends UserJourneyBase {


    @Test
    public void testCreateAccount() throws Exception {
        String jan = "Jan Plewka";

        admin
                .there_is_no_account_for(jan);

        user
                .is_logged_out()
                .logs_in_with(jan, SOME_PASSWORD);

        screen
                .shows_error_alert("login attempt was not successful");

        user
                .creates_an_account(jan, SOME_EMAIL, SOME_PASSWORD, SOME_STREET, SOME_STREET, SOME_CITY, SOME_POSTCODE, SOME_STATEPROVINCE, EMPTY_COUNTRY, EMPTY_PASSWORD, SOME_CONFIRMEDPASSWORD);

        screen
                .shows_error_alert("There were errors");

        user
                .creates_an_account(jan, SOME_EMAIL, SOME_PASSWORD, SOME_STREET, SOME_STREET, SOME_CITY, SOME_POSTCODE, SOME_STATEPROVINCE, VALID_COUNTRY, SOME_PHONE_NUMBER, SOME_CONFIRMEDPASSWORD);
        screen
                .shows_message("new account has been created");

        user
                .creates_an_account(jan, SOME_EMAIL, SOME_PASSWORD, SOME_STREET, SOME_STREET, SOME_CITY, SOME_POSTCODE, SOME_STATEPROVINCE, VALID_COUNTRY, SOME_PHONE_NUMBER, SOME_INVALID_CONFIRMEDPASSWORD);

        screen
                .shows_error_alert("There were errors");

        user
                .is_logged_out()
                .logs_in_with(jan, SOME_PASSWORD);

        screen
                .shows_in_navbar("Welcome " + jan);
    }

    @Test
    public void testAccessRights() throws Exception {
        String Hugo = "Hugo Huser";
        String Arno = "Arno Admin";

        String item = "Test ITEM";


        admin
                .there_is_a_user(Hugo, SOME_PASSWORD)
                .there_is_an_admin(Arno, SOME_PASSWORD)
                .there_is_no_item(item)
                .there_is_a_frame(item,10l);

        user
                .is_logged_out()
                .visits_his_profile();
        screen
                .shows_login();

        user
                .logs_in_with(Hugo, SOME_PASSWORD)
                .visits_his_profile();
        screen
                .shows_profile_for(Hugo);
        user
                .visits_admin_profile();
        screen
                .shows_error_alert("access is denied");
        user
                .logs_in_with(Arno, SOME_PASSWORD)
                .visits_admin_profile();
        screen
                .shows_admin_profile();
        user
                .visits_profile_for(Hugo);
        screen
                .shows_profile_for(Hugo);

        user
                .is_logged_out()
                .add_item_to_shopping_cart(item);
        screen
                .shows_login();

        user
                .is_logged_out()
                .logs_in_with(Hugo, SOME_PASSWORD)
                .visits_shopping_cart();
        screen
                .shows_shopping_cart();
    }

    @Test
    public void shouldDisplayErrorMessageWhenAccessingOtherUserProfileViaURL() throws InterruptedException {
        String userAccount = "Hugo Huser";
        String adminAccount = "Arno Admin";

        admin
                .there_is_a_user(userAccount, SOME_PASSWORD)
                .there_is_an_admin(adminAccount, SOME_PASSWORD);

        user
                .is_logged_out()
                .visits_his_profile();
        screen
                .shows_login();

        user
                .logs_in_with(userAccount, SOME_PASSWORD)
                .tries_to_view_profile_of(adminAccount);
        screen
                .shows_error_alert("access is denied");
    }


    @Test
    public void shouldDisplayTheCountryOfUserWhenUserProfileIsClicked() throws Exception {
        String jan = "Jan Plewka";

        admin
                .there_is_a_user(jan, SOME_PASSWORD);
        user
                .is_logged_out()
                .logs_in_with(jan, SOME_PASSWORD)
                .visits_his_profile();

        screen
                .shows_profile_for(jan)
                .should_show_country();
    }

    @Test
    public void shouldDisplayTheCountryOfAdminWhenAdminProfileIsClicked() throws Exception {
        String Arno = "Arno Admin";

        admin
                .there_is_an_admin(Arno, SOME_PASSWORD);
        user
                .is_logged_out()
                .logs_in_with(Arno, SOME_PASSWORD)
                .visits_his_profile();

        screen
                .shows_profile_for(Arno)
                .should_show_country();
    }

    @Test
    public void shouldDisplayPhoneNumberWhenUserProfileIsClicked() throws Exception {
        String Arno = "Arno Admin";
        String Jan = "Jan User";

        admin
                .there_is_an_admin(Arno, SOME_PASSWORD)
                .there_is_a_user(Jan, SOME_PASSWORD);
        user
                .is_logged_out()
                .logs_in_with(Arno, SOME_PASSWORD)
                .visits_his_profile();
        screen
                .shows_profile_for(Arno)
                .should_show_phone_number();
        user
                .logs_in_with(Jan, SOME_PASSWORD)
                .visits_his_profile();
        screen
                .shows_profile_for(Jan)
                .should_show_phone_number();
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
