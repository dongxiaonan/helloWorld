package functional.com.trailblazers.freewheelers;

import com.trailblazers.freewheelers.FreeWheelersServer;
import org.junit.Test;

import static functional.com.trailblazers.freewheelers.helpers.SyntaxSugar.ONLY_ONE_LEFT;
import static functional.com.trailblazers.freewheelers.helpers.SyntaxSugar.SOME_PASSWORD;

public class ShoppingCartTest extends UserJourneyBase{

    @Test
    public void addToCartProcessTest() throws Exception{
        String Arno = "Arno Admin";
        String Bob = "Bob Buyer";
        String Simplon_Frame = "Simplon Pavo 3 Ultra " + System.currentTimeMillis();

        admin
                .there_is_a_user(Bob, SOME_PASSWORD)
                .there_is_a_frame(Simplon_Frame, ONLY_ONE_LEFT);

        user
                .logs_in_with(Bob, SOME_PASSWORD)
                .visits_home_page();

        screen
                .should_list_item(Simplon_Frame);

        user
                .add_item_to_shopping_cart(Simplon_Frame);
        screen
                .shouldShowSuccessAtHomePageForAddingItemToCart();
        user
                .visits_home_page()
                .visits_shopping_cart();
        screen
                .should_list_item_in_shopping_cart(Simplon_Frame);
        user
                .check_out_item();
        screen
                .should_visit_confirmation_page();
    }

    @Test
    public void addToCartThenClearTest() throws Exception{
        String Bob = "Bob Buyer";
        String Simplon_Frame = "Simplon Pavo 3 Ultra " + System.currentTimeMillis();

        admin
                .there_is_a_user(Bob, SOME_PASSWORD)
                .there_is_a_frame(Simplon_Frame, ONLY_ONE_LEFT);

        user
                .logs_in_with(Bob, SOME_PASSWORD)
                .visits_home_page();

        screen
                .should_list_item(Simplon_Frame);

        user
                .add_item_to_shopping_cart(Simplon_Frame)
                .visits_shopping_cart();
        screen
                .should_list_item_in_shopping_cart(Simplon_Frame);
        user
                .clickClearItemsInShoppingCart();
        screen
                .shouldGoToHomePage();
        user
                .visits_shopping_cart();
        screen
                .should_not_list_item_in_shopping_cart(Simplon_Frame);

    }

    @Test
    public void addMultipleItemsToTheCart() throws Exception {
        if(FreeWheelersServer.enabledFeatures.contains("multipleItemsPerCart")) {
            String Bob = "Bob Buyer";
            String Simplon_Frame = "Simplon Pavo 3 Ultra " + System.currentTimeMillis();
            String ViewerFrame = "Viewer 3" + System.currentTimeMillis();

            admin
                    .there_is_a_user(Bob, SOME_PASSWORD)
                    .there_is_a_frame(Simplon_Frame, ONLY_ONE_LEFT)
                    .there_is_a_frame(ViewerFrame, ONLY_ONE_LEFT);
            user
                    .logs_in_with(Bob, SOME_PASSWORD)
                    .visits_home_page();

            screen
                    .should_list_item(Simplon_Frame)
                    .should_list_item(ViewerFrame);

            user
                    .add_item_to_shopping_cart(Simplon_Frame)
                    .add_item_to_shopping_cart(ViewerFrame)
                    .visits_shopping_cart();
            screen
                    .should_list_item_in_shopping_cart(Simplon_Frame)
                    .should_list_item_in_shopping_cart(ViewerFrame);
        }
    }

}
