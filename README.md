# Automation-QA-template-gradle-project
A template used for creating Automation QA projects, using gradle

# To edit the project name and packajes
![image](https://github.com/kristiyanbstoychev/Automation-QA-template-gradle-project/assets/77746043/b46b54c7-ca76-458a-927f-20c476865c8a)
![image](https://github.com/kristiyanbstoychev/Automation-QA-template-gradle-project/assets/77746043/00ad855c-98f2-43f0-afeb-1547174b5361)
![image](https://github.com/kristiyanbstoychev/Automation-QA-template-gradle-project/assets/77746043/b62efe62-824b-46dc-9eac-377d42c48e04)
![image](https://github.com/kristiyanbstoychev/Automation-QA-template-gradle-project/assets/77746043/6a029da0-c13e-41f0-ae18-5ed94dad63ab)

# How produce a cleaner and a better looking code
## Automatically reformat code on save﻿:
You can configure the IDE to reformat code in modified files automatically when your changes are saved.
Press Ctrl + Alt + S to open the IDE settings and then select Tools | Actions on Save. Enable the Reformat code option.

![image](https://github.com/kristiyanbstoychev/Automation-QA-template-gradle-project/assets/77746043/8edff08f-f45b-421c-a960-1e4754c75f34)

# Example Automation Testing Websites For Practice purposes
  https://demoqa.com/

  https://bstackdemo.com
  
  https://www.demoblaze.com/

# CI/CD Setup
To run any given Test Class from the command line

```
./gradlew -i clean test --tests 'TestSuiteRunnerCFUI' - to run jUnit tests with Gradle
```

Jenkins setup
In the Build steps section, select Execute Shell and type:
```
#!/bin/bash
source ~/.bashrc
source ~/.profile
cd path/to/the/local/repository
export URL_FOR_TESTING="url for testing"
export BROWSER_FOR_TESTING="browser"
export DEVICE_FOR_TESTING="device"
./gradlew -i clean test --tests 'TestSuiteRunnerCFUI'
```

In the Build Triggers section select Build periodically, to run the test on a schedule
Example: to run the Tests at 17:50 
```
50 17 * * *
```
  # Architecture
  ![image](https://github.com/kristiyanbstoychev/Automation-QA-template-gradle-project/assets/77746043/5d6bee03-b09a-4d56-8096-15f2a91dff6c)

  The project is based on the Page object model. Tests and locators are created in separate classes.
  The PAGES package contains the locators and methods needed for testing a certain page. Example: LoginPage
  The TESTS package contains sets of tests that verify certain functionalities. Example LoginTests
  In each of theese packages there is a Base class (Page or Test), that contains commonly used code between the rest of the tests or pages.
  
  ## BaseTest class breakdown
  ### Tests setup:
  Before the start of each test, the pickBrowser method is called. The method is repsonsible for determening the browser and device used for testing.
  In order for the tests to be easily implemented with a CI/CD environment a set of globally set variables is used (BROWSER_FOR_TESTING and DEVICE_FOR_TESTING).
  Default values are set for both variables.
  After the browser and device for testing are determined a set of certaing browser capabillities are applied.
    
    //Picks which browser and browser options should be used for testing
    public static void pickBrowser(String browser) {
        switch (browser) {
            case "chrome": {
                webDriverManager = WebDriverManager.chromedriver();
                webDriverManager.setup();
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
                driver = new ChromeDriver(chromeOptions);
                break;
            }
            case "firefox": {
                webDriverManager = WebDriverManager.firefoxdriver();
                webDriverManager.setup();
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
                webDriverManager.setup();
                SafariOptions safariOptions = new SafariOptions();
                break;
            }
            case "mobile": {
                webDriverManager = WebDriverManager.chromedriver();
                webDriverManager.setup();
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
                mobileOptions.addArguments("--remote-allow-origins=*");
                mobileOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                driver = new ChromeDriver(mobileOptions);
                break;
            }
        }
    }

    
  The browser is opened in headless mode (no UI) in incognito mode, that way the testing environment will remain the same each time a test is run.
  Next the setEmailAndNotificationsContent() method is called. This function is responsible for managing the content of the notifications throughout the     website
  (onsite, email and pop-ups)
  Next readCSVFileAndSaveContentAsListOfStrings() method is called, which reads a specific csv file and saves its content to a list of Strings.
  
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
        getWait().until(ExpectedConditions.presenceOfElementLocated((By.xpath("xpath of an element that should be displayed on the home page"))));
        //Sets the content of email and onsite notifications
        GlobalVariables.setEmailAndNotificationsContent();
        //Reads a couple of csv files, storing emails and nicknames of users, needed for testing
        CSVHandler.readCSVFileAndSaveContentAsListOfStrings("testData/emails.csv", csvEmailsList);
        CSVHandler.readCSVFileAndSaveContentAsListOfStrings("testData/nicknames.csv", csvNicknamesList);
    }
  
  The class can also contain other methods, which are commonly used among the other test classes.
  
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
  
  
  ## BasePage class breakdown:
  This class contains commonly used locators and methods for other pages
  
  public class ExampleBasePageCFUI {

    @FindBy(xpath = "//button[@data-testid='exampleId']")
    private static WebElement exampleWebElement;

    public static WebElement getExampleWebElement() {
        return exampleWebElement;
    }
}
  
  ## Global variables
  A class containing variables that are used throught the tests
  
    //---------Test setup variables---------//
    //variables storing the urls for testing
    public final static String cfuiURL = "https://example.com";
    public final static String boURL = "https://example.com/";

    //variable storing the currently used operating system
    public final static String operationSystem = System.getProperty("os.name");

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
 
   
 ## Example Locators and page methods class
 
 public class ExampleBasePageCFUI {
    //Locators
    @FindBy(xpath = "//button[@data-testid='exampleId']")
    private static WebElement exampleWebElement;

    public static WebElement getExampleWebElement() {
        return exampleWebElement;
    }

    //Tests
    public void classSpecificMethod() {
        //....
    }
    }
    
 ## Example Tests class
 
 public class ExampleTestsPageCFUI extends ExampleBaseTestCFUI {

    @Test
    void exampleTest() {
        ExampleLocatorsCFUI exampleLocatorsCFUI = PageFactory.initElements(driver, ExampleLocatorsCFUI.class);
        exampleLocatorsCFUI.exampleTestMethod();
    }
    }
    
 ## Example Base API Tests class   
    
    public class ExampleBaseAPITestsCFUI {

    //Stores cookies for requests
    static CookieJar cookieJar = new CookieJar() {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, @NotNull List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<>();
        }
    };
    }

 ## Utils package
 ### CSV Handler
   
    public class CSVHandler {
    public static final String delimiter = ",";

    //reads any given csv file and saves it into a specified list of strings
    public static void readCSVFileAndSaveContentAsListOfStrings(String csvFile, List<String> csvData) {
        try {
            File file = new File(csvFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String[] csvArray;
            String line = " ";
            while ((line = bufferedReader.readLine()) != null) {
                csvArray = line.split(delimiter);
                csvData.addAll(Arrays.asList(csvArray));
            }
            bufferedReader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    }
    
 ### Notifications Handler
        //Method used for verifying the content of email notifications,
        //if an email is not received for up to one minute an error message is displayed
        public static void openMailinatorMailInbox(String mailinatorUser, String receivedEmail, String emailBodyText, String emailButton) {
        driver.get("https://mailinator.com/v4/public/inboxes.jsp?to=" + mailinatorUser);

        System.out.println("************* Waiting for the email to arrive *************");

        if (isMobile) {
            getWait(60).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'just now')]")));
            getWait(60).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(), '" + receivedEmail + "')]")));
            driver.findElement(By.xpath("//a[contains(text(), '" + receivedEmail + "')]")).click();

        } else {
            getWait(50).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(), 'just now')]")));
            getWait(50).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(), '" + receivedEmail + "')]")));
            driver.findElement(By.xpath("//td[contains(text(), '" + receivedEmail + "')]")).click();
        }

        driver.switchTo().frame(driver.findElement(By.id("html_msg_body")));
        TimeUtils.waitForSeconds(2);
        getWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//td[contains(text(), '" + emailBodyText + "')]"))));
        System.out.println("************* Email content verified: " + emailBodyText + " *************");

        if ((driver.findElement(By.xpath("//a[contains(text(), '" + emailButton + "')]"))).isDisplayed()) {
            driver.findElement(By.xpath("//a[contains(text(), '" + emailButton + "')]")).click();
            System.out.println("************* Email button clicked *************");
            ArrayList<String> arrayList = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(arrayList.get(1));
        }
        }

### TestResultsWatcher

    public class TestResultsWatcher implements TestWatcher {

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        driver.quit();
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        driver.quit();
    }

    //takes a screenshot, if a test fails and saves it in the system temp folder
    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        String screenshotFileName = "";
        if (operationSystem.contains("Windows")) {
            screenshotFileName = localTempDirectoryWindows + extensionContext.getDisplayName() + "_" + TestDataGeneration.formatDate("hh-mm-ss_dd.MM", "bg") + ".jpeg";
        }
        if (operationSystem.contains("Linux")) {
            screenshotFileName = localTempDirectoryLinux + extensionContext.getDisplayName() + "_" + TestDataGeneration.formatDate("hh-mm-ss_dd.MM", "bg") + ".jpeg";
        }

        takeSnapShot(driver, screenshotFileName);
        System.out.println("Test failed! Screenshot saved to: " + screenshotFileName);

        driver.quit();
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        driver.quit();
    }

    //method used for taking screenshots
    public static void takeSnapShot(WebDriver webdriver, String fileWithPath) {
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);

        //Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File DestFile = new File(fileWithPath);

        //Copy file at destination
        try {
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

    
### TimeUtils

    public class TimeUtils {

    public static void waitForSeconds(int timeoutInSeconds) {
        try {
            Thread.sleep(timeoutInSeconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clickAvailableElementFromList(List<WebElement> webElementList) {
        int i = 0;
        while (!webElementList.get(i).isDisplayed()) {
            System.out.println("************* element not clickable...retrying *************");
            i++;
            if (i > 5) {
                System.out.println("************* element cannot be found *************");
                break;
            }
        }
        scrollIntoView(webElementList.get(i));

        webElementList.get(i).click();
    }

    public static void clickAvailableElement(WebElement webElement) {
        int i = 0;
        while (!webElement.isDisplayed()) {
            System.out.println("element not clickable");
            i++;
            if (i >= 5) {
                System.out.println("element cannot be found");
                break;
            }
        }
        scrollIntoView(webElement);
        webElement.click();
    }
    }
