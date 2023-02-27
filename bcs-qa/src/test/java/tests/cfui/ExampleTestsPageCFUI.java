package tests.cfui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import pages.cfui.ExampleLocatorsCFUI;

import static tests.GlobalVariables.driver;


public class ExampleTestsPageCFUI extends ExampleBaseTestCFUI {

    @Test
    void exampleTest() {
        ExampleLocatorsCFUI exampleLocatorsCFUI = PageFactory.initElements(driver, ExampleLocatorsCFUI.class);
        exampleLocatorsCFUI.exampleTestMethod();
    }
}
