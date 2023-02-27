package pages.cfui;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExampleLocatorsCFUI extends ExampleBasePageCFUI {

    //Locators
    @FindBy(xpath = "//a[@data-testid='navDesktopLoginLink']")
    private WebElement exampleElement;

    public WebElement getExampleElement() {
        return exampleElement;
    }

    //Test methods
    public void exampleTestMethod() {

    }
}
