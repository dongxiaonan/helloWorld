package functional.com.trailblazers.freewheelers.helpers;

import org.openqa.selenium.By;

public class ShoppingCartTable {
    public static By nameFieldFor(String item) {
        return By.xpath("//tbody/tr/td[1][text() = '" + item + "']");
    }

}
