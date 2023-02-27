package pages.cfui;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExampleBasePageCFUI {

    @FindBy(xpath = "//button[@data-testid='exampleId']")
    private static WebElement exampleWebElement;

    public static WebElement getExampleWebElement() {
        return exampleWebElement;
    }
}
