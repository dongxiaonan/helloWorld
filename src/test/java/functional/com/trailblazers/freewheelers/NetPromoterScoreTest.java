package functional.com.trailblazers.freewheelers;

import functional.com.trailblazers.freewheelers.helpers.FeedbackType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NetPromoterScoreTest extends UserJourneyBase {

    private final String adminUser = "NPSAdmin";
    private final String password = "V3ry Secure!";
    private final String username = "Promoter";
    private final String frame = "Bike Frame";
    private final long someAccountId = 12L;

    @Before
    public void setUp(){
        long quantity = 10;
        admin
                .there_is_an_admin(adminUser, password)
                .there_is_a_frame(frame, quantity)
                .there_is_a_user(username, password);
    }

    @Test
    public void testSurveyPopUp() throws InterruptedException {
        user
                .logs_in_with(username, password)
                .visits_home_page()
                .add_item_to_shopping_cart(frame)
                .visits_shopping_cart()
                .check_out_item()
                .waits_for_survey_popup();

        npsSurveyForm.submitFeedback(FeedbackType.Positive, "Some Feedback");
        Assert.assertTrue(npsSurveyForm.thankYouMessageExists());
        npsSurveyForm.closeWindow();

        user.is_logged_out();
    }

    @Test
    public void shouldShowNPSReport() {

        admin
                .there_are_no_survey_entries()
                .there_is_a_survey_entry_for(someAccountId, 10, "Some Feedback")
                .there_is_a_survey_entry_for(someAccountId, 10, "Some Positive Feedback")
                .there_is_a_survey_entry_for(someAccountId, 7, "Some Passive Feedback")
                .there_is_a_survey_entry_for(someAccountId, 0, "Some Negative Feedback");

        //unlogged in user
        user
                .is_logged_out()
                .visits_nps_report_page_by_link();
        screen
                .shows_login()
                .should_not_contain_nps_report_link_in_header();

        //unauthorised user
        user.logs_in_with(username, password);
        screen
                .shouldGoToHomePage()
                .should_not_contain_nps_report_link_in_header();

        //show survey report
        user
                .logs_in_with(adminUser, password)
                .visits_nps_report_page();

        assertTrue(npsReportPage.totalResponsesCorrect("4"));
        assertTrue(npsReportPage.npsScoreCorrect("25"));
        assertTrue(npsReportPage.percentagePromotersCorrect("50"));
        assertTrue(npsReportPage.percentageDetractorsCorrect("25"));
        assertTrue(npsReportPage.percentagePassivesCorrect("25"));
        assertTrue(npsReportPage.containsPromoterFeedbackComment("Some Positive Feedback"));
        assertTrue(npsReportPage.containsDetractorFeedbackComment("Some Negative Feedback"));
        assertTrue(npsReportPage.containsPassiveFeedbackComment("Some Passive Feedback"));
    }

    @After
    public void tearDown() {
        admin
                .there_are_no_survey_entries()
                .there_is_no_item(frame)
                .there_is_no_account_for(adminUser)
                .there_is_no_account_for(username);
    }
}
