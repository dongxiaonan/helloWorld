package functional.com.trailblazers.freewheelers.helpers;

import org.openqa.selenium.By;

public class OrderTable {

    public static By selectFor(String item) {
        return By.xpath("//tbody/tr/td[3]/table/tbody/tr/td[text() = '" + item + "']/../../../../../td[5]/select");
    }

    public static By saveButtonFor(String item) {
        return By.xpath("//tbody/tr/td[3]/table/tbody/tr/td[text() = '" + item + "']/../../../../../td[7]/button");
    }
}
