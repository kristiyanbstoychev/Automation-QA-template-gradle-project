import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariOptions;
import tests.GlobalVariables;
import utils.CSVHandler;

import java.util.HashMap;
import java.util.Map;

import static tests.GlobalVariables.browserForTestingEnvVariableName;
import static tests.GlobalVariables.cfuiURL;
import static tests.GlobalVariables.csvEmailsList;
import static tests.GlobalVariables.csvNicknamesList;
import static tests.GlobalVariables.defaultBrowser;
import static tests.GlobalVariables.defaultDevice;
import static tests.GlobalVariables.deviceForTestingEnvVariableName;
import static tests.GlobalVariables.getEnvVariable;
import static tests.GlobalVariables.isDebugModeEnabled;
import static tests.GlobalVariables.isMobile;
import static tests.GlobalVariables.urlForTestingEnvVariable;
import static tests.GlobalVariables.verificationCode;
import static tests.GlobalVariables.verificationCodeArray;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests {
    public WebDriver parallelDriver;
    public WebDriverManager parallelWebDriverManager;

    //Executed before each test
    @BeforeEach
    public void doBeforeEachParallelTest() {
        setupParallelBrowsers(getEnvVariable(browserForTestingEnvVariableName, defaultBrowser));

        if (getEnvVariable(urlForTestingEnvVariable, cfuiURL).contains("admin")) {
            //BO Locator
        } else {
          // CFUI Locator
        }

        //Sets the content of email and onsite notifications
        GlobalVariables.setEmailAndNotificationsContent();

        //Reads a couple of csv files, storing emails and nicknames of users, needed for testing
        CSVHandler.readCSVFileAndSaveContentAsListOfStrings("testData/emails.csv", csvEmailsList);
        CSVHandler.readCSVFileAndSaveContentAsListOfStrings("testData/nicknames.csv", csvNicknamesList);

        verificationCode = " ";
        verificationCodeArray.clear();
    }

    public void setupParallelBrowsers(String browser) {
        switch (browser) {
            case "chrome" -> {
                parallelWebDriverManager = WebDriverManager.chromedriver();
                parallelWebDriverManager.setup();
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
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                parallelDriver = new ChromeDriver(chromeOptions);
            }
            case "firefox" -> {
                parallelWebDriverManager = WebDriverManager.firefoxdriver();
                parallelWebDriverManager.setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--incognito");
                if (!isDebugModeEnabled) {
                    firefoxOptions.addArguments("--headless", "--window-size=1920,1200");
                }
                firefoxOptions.addArguments("--lang=en_US");
                firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                parallelDriver = new FirefoxDriver(firefoxOptions);
            }
            case "safari" -> {
                parallelWebDriverManager = WebDriverManager.safaridriver();
                parallelWebDriverManager.setup();
                SafariOptions safariOptions = new SafariOptions();
            }
            case "mobile" -> {
                parallelWebDriverManager = WebDriverManager.chromedriver();
                parallelWebDriverManager.setup();
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", getEnvVariable(deviceForTestingEnvVariableName, defaultDevice));
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
                mobileOptions.addArguments("--remote-allow-origins=*");
                mobileOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                parallelDriver = new ChromeDriver(mobileOptions);
            }
        }
        if (getEnvVariable(browserForTestingEnvVariableName, defaultBrowser).equals("mobile")) {
            isMobile = true;
        }

        parallelDriver.manage().window().maximize();
        parallelDriver.manage().deleteAllCookies();
        parallelDriver.navigate().to(getEnvVariable(urlForTestingEnvVariable, cfuiURL));
    }

    @AfterEach
    public void tearDown() {
        parallelDriver.quit();
    }

    //Pick how many times the test should be executed, by changing the RepeatedTest parameter
    @RepeatedTest(value = 3, name = "Parallel test #{currentRepetition}")
    public void loginMultipleUsers(RepetitionInfo repetitionInfo) {
        parallelDriver.navigate().to("https://stackoverflow.com/questions/63058093/gradle-running-tests-in-parallel-from-one-class");
    }
}

