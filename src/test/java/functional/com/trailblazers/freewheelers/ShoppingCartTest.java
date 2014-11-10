package functional.com.trailblazers.freewheelers;

import com.trailblazers.freewheelers.FreeWheelersServer;
import functional.com.trailblazers.freewheelers.apis.ScreenApi;
import functional.com.trailblazers.freewheelers.apis.UserApi;
import org.junit.Test;

import static functional.com.trailblazers.freewheelers.helpers.SyntaxSugar.*;
import static java.lang.Thread.sleep;

public class ShoppingCartTest extends UserJourneyBase {

    @Test
    public void addToCartProcessTest() throws Exception {
        String Arno = "Arno Admin";
        String Bob = "Bob Buyer";
        String Simplon_Frame = "Simplon Pavo 3 Ultra " + System.currentTimeMillis();

        sleep(1000);
        admin
                .there_is_an_admin(Arno, SOME_PASSWORD)
                .there_is_a_user(Bob, SOME_PASSWORD)
                .there_is_a_frame(Simplon_Frame, ONLY_ONE_LEFT);
        user
                .is_logged_out()
                .visits_home_page()
                .add_item_to_shopping_cart(Simplon_Frame)
                .logs_in_with(Bob, SOME_PASSWORD);
//        screen
//                .shouldShowSuccessAtHomePageForAddingItemToCart();

        user
                .visits_shopping_cart();
        screen
                .should_list_item_in_shopping_cart(Simplon_Frame);
        user
                .clickClearItemsInShoppingCart()
                .add_item_to_shopping_cart(Simplon_Frame);
        screen
                .shouldShowSuccessAtHomePageForAddingItemToCart();
        user
                .visits_shopping_cart();
        screen
                .should_list_item_in_shopping_cart(Simplon_Frame);
        user
                .check_out_item();
        if (FreeWheelersServer.enabledFeatures.contains("cardPayment")){
            cardPaymentTest(Bob, user, screen);
        }
        screen
                .should_visit_confirmation_page();
        user
                .visits_home_page();
        screen
                .should_not_list_item(Simplon_Frame);
        user
                .logs_in_with(Arno, SOME_PASSWORD)
                .visits_admin_profile();
        screen
                .there_should_be_an_order(Simplon_Frame, "NEW");
        admin
                .changes_order_status(Simplon_Frame, "IN_PROGRESS");
        screen
                .there_should_be_an_order(Simplon_Frame, "IN_PROGRESS");
    }


    @Test
    public void addMultipleItemsToTheCartAndCheckout() throws Exception {
        if (FreeWheelersServer.enabledFeatures.contains("multipleItemsPerCart")) {
            String Bob = "Bob Buyer";
            String Xman_Frame = "Xman Pavo 3 Ultra " + System.currentTimeMillis();
            String ViewerFrame = "Viewer 3" + System.currentTimeMillis();

            admin
                    .there_is_a_user(Bob, SOME_PASSWORD)
                    .there_is_a_frame(Xman_Frame, ONLY_ONE_LEFT)
                    .there_is_a_frame(ViewerFrame, ONLY_ONE_LEFT);
            user
                    .logs_in_with(Bob, SOME_PASSWORD)
                    .visits_home_page();

            screen
                    .should_list_item(Xman_Frame)
                    .should_list_item(ViewerFrame);
            user
                    .add_item_to_shopping_cart(Xman_Frame)
                    .add_item_to_shopping_cart(ViewerFrame)
                    .visits_shopping_cart();
            screen
                    .should_list_item_in_shopping_cart(Xman_Frame)
                    .should_list_item_in_shopping_cart(ViewerFrame);
            user
                    .check_out_item();
            if (FreeWheelersServer.enabledFeatures.contains("cardPayment")){
                user
                        .fill_in_card_details("Bob", VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV)
                        .click_submit_button_for_card_payment();
            }
            screen
                    .should_visit_confirmation_page()
                    .should_list_item_in_shopping_cart(Xman_Frame)
                    .should_list_item_in_shopping_cart(ViewerFrame)
                    .should_visit_confirmation_page();
            user
                    .visits_his_profile();
            screen
                    .shouldShowListOfOrderedItems(Xman_Frame, ViewerFrame);
        }
    }

    private void cardPaymentTest(String Bob, UserApi user, ScreenApi screen) {
        user
                .fill_in_card_details(Bob, SOME_CARD_NUMBER+"a", SOME_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV)
                .click_submit_button_for_card_payment();
        screen
                .should_show_card_fields("", SOME_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV)
                .shows_error("Only numbers are allowed");

        user
                .fill_in_card_details(Bob, SOME_CARD_NUMBER, SOME_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV)
                .click_submit_button_for_card_payment();
        screen
                .should_show_card_fields("", SOME_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV)
                .shows_error("The length of card number must be 16");

        user
                .fill_in_card_details(Bob, VALID_CARD_NUMBER, "Month", VALID_EXPIRAY_YEAR, VALID_CCV)
                .click_submit_button_for_card_payment();
        screen
                .should_show_card_fields(VALID_CARD_NUMBER, "", VALID_EXPIRAY_YEAR, VALID_CCV)
                .shows_error("You need to select month and year again");

        user
                .fill_in_card_details(Bob, VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, "Year", VALID_CCV)
                .click_submit_button_for_card_payment();
        screen
                .should_show_card_fields(VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, "", VALID_CCV)
                .shows_error("You need to select month and year again");

        user
                .fill_in_card_details(Bob, VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV+"a")
                .click_submit_button_for_card_payment();
        screen
                .should_show_card_fields(VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, VALID_EXPIRAY_YEAR, "")
                .shows_error("Only numbers are allowed");

        user
                .fill_in_card_details(Bob, VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV+"12")
                .click_submit_button_for_card_payment();
        screen
                .should_show_card_fields(VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, VALID_EXPIRAY_YEAR, "")
                .shows_error("The length of CCV must be 3 or 4");

        user
                .fill_in_card_details(Bob, VALID_CARD_NUMBER, VALID_EXPIRAY_MONTH, VALID_EXPIRAY_YEAR, VALID_CCV)
                .click_submit_button_for_card_payment();
        screen
                .should_confirm_payment();
    }
}
