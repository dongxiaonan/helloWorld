package functional.com.trailblazers.freewheelers.helpers;

import org.openqa.selenium.By;

public class ItemsTable {
    public static By nameFieldFor(String item) {
        return By.xpath("//tbody/tr/td[1][text() = '" + item + "']");
    }
    public static By items() {
        return By.tagName("tr");
    }

    public static By firstColumnOfRow() {
        return By.xpath("//td[1]");
    }
}
