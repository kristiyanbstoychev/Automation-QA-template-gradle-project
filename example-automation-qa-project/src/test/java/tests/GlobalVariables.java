package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import utils.TestDataGeneration;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables {

    //---------Test setup variables---------//
    //variables storing the urls for testing
    public final static String cfuiURL = "https://example.com";
    public final static String boURL = "https://example.com/";

    //variable storing the currently used operating system
    public static String operationSystem = System.getProperty("os.name");

    public final static String localTempDirectory = System.getProperty("java.io.tmpdir") + "01_failedTestsScreenshots\\";

    public static WebDriver driver;

    //default browser and device, used in cases where no environment variables are defined
    public static final String defaultBrowser = "mobile";
    public static final String defaultDevice = "iPhone 6/7/8";
    //Samsung Galaxy S20 Ultra
    //Galaxy S9+
    //iPhone XR
    //iPhone 6/7/8

    //environment variable names
    public static final String browserForTestingEnvVariableName = "BROWSER_FOR_TESTING";
    public static final String deviceForTestingEnvVariableName = "DEVICE_FOR_TESTING";

    //saves the pre-defined browser for testing environment variable to a string
    public static String getBrowserForTestingEnvVariable(String environmentVariable) {
        String browser = System.getenv(environmentVariable);
        if (browser == null || isDebugModeEnabled) {
            browser = defaultBrowser;
        }
        return browser;
    }

    //saves the pre-defined device for testing environment variable to a string
    public static String getDeviceForTestingEnvVariable(String environmentVariable) {
        String device = System.getenv(environmentVariable);
        if (device == null || isDebugModeEnabled) {
            device = defaultDevice;
        }
        return device;
    }

    //Boolean used to trigger the mobile version of the tests
    public static boolean isMobile = false;

    //driver manager, responsible for downloading the necessary webdriver executable, for running the tests
    public static WebDriverManager webDriverManager;

    //boolean which checks whether tests are executed in debug mode or not
    public static final boolean isDebugModeEnabled = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp");

    //lists with emails and nicknames that could be used for testing, stored in csv files
    public static List<String> csvEmailsList = new ArrayList<>();
    public static List<String> csvNicknamesList = new ArrayList<>();

    //Email Notifications
    public static String emailSubject = "emailSubject";
    public static String emailBody = "emailBody";
    public static String emailButton = "emailButton";

    public static JSONObject registrationEmail = new JSONObject();

    //Method storing the content of the email and onsite notifications
    public static void setEmailAndNotificationsContent() {
        //Email Notifications
        registrationEmail.put(emailSubject, "Example email subject");
        registrationEmail.put(emailBody, "Example email body");
        registrationEmail.put(emailButton, "Example email button");
    }

    public static String exampleGlobalVariable = "";
}
