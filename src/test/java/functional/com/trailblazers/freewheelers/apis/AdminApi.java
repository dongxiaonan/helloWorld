package functional.com.trailblazers.freewheelers.apis;

import com.trailblazers.freewheelers.model.*;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.SurveyService;
import com.trailblazers.freewheelers.service.impl.AccountServiceImpl;
import com.trailblazers.freewheelers.service.impl.ItemServiceImpl;
import functional.com.trailblazers.freewheelers.helpers.OrderTable;
import org.openqa.selenium.WebDriver;

import static functional.com.trailblazers.freewheelers.helpers.Controls.select;
import static functional.com.trailblazers.freewheelers.helpers.SyntaxSugar.*;


public class AdminApi {

    private WebDriver driver;
    private AccountService accountService;
    private ItemService itemService;
    private SurveyService surveyService;

    public AdminApi(WebDriver driver) {
        this.driver = driver;
        this.accountService = new AccountServiceImpl();
        this.itemService = new ItemServiceImpl();
        this.surveyService = new SurveyService();
    }

    public AdminApi there_are_no_survey_entries() {
        surveyService.deleteAll();
        return this;
    }

    public AdminApi there_is_no_account_for(String accountName) {
        Account account = accountService.getAccountByName(accountName);
        if (account != null) {
            accountService.delete(account);
        }

        return this;
    }

    public AdminApi there_is_a_user(String userName, String password) {
        there_is_no_account_for(userName);
        accountService.createAccount(account_for(userName, password));

        return this;
    }

    public AdminApi there_is_an_admin(String userName, String password) {
        there_is_no_account_for(userName);
        accountService.createAdmin(account_for(userName, password));

        return this;
    }

    public AdminApi there_is_no_item(String itemName) {
        Item toBeDeleted = itemService.getByName(itemName);

        while (toBeDeleted != null) {
            itemService.delete(toBeDeleted);
            toBeDeleted = itemService.getByName(itemName);
        }

        return this;
    }

    public AdminApi there_is_a_frame(String itemName, Long quantity) {
        there_is_no_item(itemName);
        itemService.saveItem(itemFor(itemName, quantity));

        return this;
    }

    private Item itemFor(String itemName, Long quantity) {
        return new Item()
                .setName(itemName)
                .setQuantity(quantity)
                .setDescription("A very nice item.")
                .setPrice(SOME_PRICE)
                .setType(ItemType.FRAME);
    }

    private Account account_for(String userName, String password) {
        return new Account()
                .setAccount_name(userName)
                .setPassword(password)
                .setEmail_address(emailFor(userName))
                .setCountry(new Country(VALID_COUNTRY_ID, VALID_COUNTRY))
                .setStreet1(SOME_STREET)
                .setStreet2("")
                .setCity(SOME_CITY)
                .setPostcode(SOME_POSTCODE)
                .setState_Province("")
                .setPhoneNumber(SOME_PHONE_NUMBER)
                .setEnabled(true);
    }

    public AdminApi there_is_a_survey_entry_for(long accountId, int feedbackType, String comment) {
        surveyService.submitSurvey(accountId, new SurveyEntry(feedbackType, comment));
        return this;
    }

    public AdminApi changes_order_status(String item, String toState) {
        select(toState, driver.findElement(OrderTable.selectFor(item)));
        driver.findElement(OrderTable.saveButtonFor(item)).click();

        return this;
    }
}
