package functional.com.trailblazers.freewheelers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HumansTxtTest {

    static WebDriver driver;

    @BeforeClass
    public static void before() {
        driver = new PhantomJSDriver(new DesiredCapabilities());
    }

    @AfterClass
    public static void after() {
        driver.close();
        driver.quit();
    }

    @Test
    public void shouldDisplayAllTheTeamMembers() {
        driver.get("http://localhost:8080/humans.txt");
        WebElement element = driver.findElement(By.tagName("pre"));
        String[] expectedTeamMembers = {"Nishitha Ningegowda",
                                        "Peter Gibbons",
                                        "Samir Nagheenanajar",
                                        "Michael Bolton",
                                        "Juliana Paukowski",
                                        "Xiaonan Dong",
                                        "Michael Bolton",
                                        "Taylar Wade",
                                        "Daisy Shih",
                                        "Siba Fadda",
                                        "Abhishek Puri",
                                        "Flavia Fale",
                                        "Gustavo Goulart",
                                        "Mohammed Binsabbar",
                                        "Florian Schlosser",
                                        "Boxing Peng",
                                        "Aleksandar Serafimoski"};
        assertThat(element.getText().split("\n"), is(expectedTeamMembers));
    }

}
