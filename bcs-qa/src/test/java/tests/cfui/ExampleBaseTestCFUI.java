package tests.cfui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestResultsWatcher;
import utils.TimeUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static tests.GlobalVariables.browserForTestingEnvVariableName;
import static tests.GlobalVariables.cfuiURL;
import static tests.GlobalVariables.deviceForTestingEnvVariableName;
import static tests.GlobalVariables.driver;
import static tests.GlobalVariables.getBrowserForTestingEnvVariable;
import static tests.GlobalVariables.getDeviceForTestingEnvVariable;
import static tests.GlobalVariables.isDebugModeEnabled;
import static tests.GlobalVariables.isMobile;
import static tests.GlobalVariables.webDriverManager;

//Execute tests in alphabetical order
@TestMethodOrder(MethodOrderer.MethodName.class)
//Enables the TestWatcher class, which monitors the test results
@ExtendWith(TestResultsWatcher.class)
public class ExampleBaseTestCFUI {

    //Executed before each test
    @BeforeEach
    public void doBeforeEachTest() {
        pickBrowser(getBrowserForTestingEnvVariable(browserForTestingEnvVariableName));

        if (getBrowserForTestingEnvVariable(browserForTestingEnvVariableName).equals("mobile")) {
            isMobile = true;
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.navigate().to(cfuiURL);
        getWait().until(ExpectedConditions.presenceOfElementLocated((By.xpath("//button[@data-testid='headerLanguage']"))));

        TimeUtils.waitForSeconds(1);
        try {
            driver.findElement(By.xpath("//button[contains(text(), 'Got it')]")).click();
        } catch (Exception e) {
            System.out.println("Cookie consent not displayed");
        }
    }

    //Picks which browser and browser options should be used for testing
    public static void pickBrowser(String browser) {
        switch (browser) {
            case "chrome": {
                webDriverManager = WebDriverManager.chromedriver();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--ignore-certificate-errors");
                //if tests are executed in debug mode, start the browser in a non-headless window
                if (!isDebugModeEnabled) {
                    chromeOptions.addArguments("--headless", "--window-size=1920,1200");
                }
                chromeOptions.addArguments("--allow-running-insecure-content");
                chromeOptions.addArguments("--lang=en_US");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("-disable-features=NetworkService");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                driver = new ChromeDriver(chromeOptions);
                break;
            }
            case "firefox": {
                webDriverManager = WebDriverManager.firefoxdriver();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--incognito");
                if (!isDebugModeEnabled) {
                    firefoxOptions.addArguments("--headless", "--window-size=1920,1200");
                }
                firefoxOptions.addArguments("--lang=en_US");
                firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                driver = new FirefoxDriver(firefoxOptions);
                break;
            }
            case "safari": {
                webDriverManager = WebDriverManager.safaridriver();
                SafariOptions safariOptions = new SafariOptions();
                break;
            }
            case "mobile": {
                webDriverManager = WebDriverManager.chromedriver();
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", getDeviceForTestingEnvVariable(deviceForTestingEnvVariableName));
                ChromeOptions mobileOptions = new ChromeOptions();
                mobileOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                mobileOptions.addArguments("--incognito");
                mobileOptions.addArguments("--ignore-certificate-errors");
                if (!isDebugModeEnabled) {
                    mobileOptions.addArguments("--headless");
                }
                mobileOptions.addArguments("--allow-running-insecure-content");
                mobileOptions.addArguments("--lang=en_US");
                mobileOptions.addArguments("--disable-gpu");
                mobileOptions.addArguments("-disable-features=NetworkService");
                mobileOptions.addArguments("--no-sandbox");
                mobileOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                driver = new ChromeDriver(mobileOptions);
                break;
            }
        }
    }

    @AfterAll
    static void doAfterAllTests() {
        //After all tests are finished, prints out all the failed tests
        System.out.println("************* Failed tests: *************");
        System.out.println(TestResultsWatcher.failedTests);
    }

    //Scrolls the page to a desired element
    public static void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        TimeUtils.waitForSeconds(3);
    }

    public static WebDriverWait getWait(long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    public static WebDriverWait getWait() {
        return getWait(15);
    }
}
