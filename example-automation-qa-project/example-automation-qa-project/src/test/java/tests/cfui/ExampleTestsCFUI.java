package tests.cfui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;
import pages.cfui.ExampleLocatorsCFUI;
import tests.ExampleBaseTest;

import static tests.GlobalVariables.driver;


public class ExampleTestsCFUI extends ExampleBaseTest {

    @Test
    void exampleTest() {
        ExampleLocatorsCFUI exampleLocatorsCFUI = PageFactory.initElements(driver, ExampleLocatorsCFUI.class);
        exampleLocatorsCFUI.exampleTestMethod();
    }
}
