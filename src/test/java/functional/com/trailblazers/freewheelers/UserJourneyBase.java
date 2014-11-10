package functional.com.trailblazers.freewheelers;

import functional.com.trailblazers.freewheelers.apis.AdminApi;
import functional.com.trailblazers.freewheelers.apis.ScreenApi;
import functional.com.trailblazers.freewheelers.apis.UserApi;
import functional.com.trailblazers.freewheelers.pages.NetPromoterScoreReportPage;
import functional.com.trailblazers.freewheelers.pages.NetPromoterScoreSurveyForm;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class UserJourneyBase {

    private static WebDriver driver;

    protected static AdminApi admin;
    protected static UserApi user;
    protected static ScreenApi screen;

    // Specific for NPS Flow
    protected static NetPromoterScoreSurveyForm npsSurveyForm;
    protected static NetPromoterScoreReportPage npsReportPage;
    private static DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    @BeforeClass
    public static void before() {
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setCapability("takesScreenshot", true);
        driver = new PhantomJSDriver(desiredCapabilities);

        admin = new AdminApi(driver);
        user = new UserApi(driver);
        screen = new ScreenApi(driver);

        // NPS Pages
        npsSurveyForm = new NetPromoterScoreSurveyForm(driver);
        npsReportPage = new NetPromoterScoreReportPage(driver);
    }

    @AfterClass
    public static void after() {
        if(driver != null) {
            driver.close();
            driver.quit();
        }
    }
}