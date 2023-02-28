# Automation-QA-template-gradle-project
A template used for creating Automation QA projects, using gradle
  
  # Architecture
  ![image](https://user-images.githubusercontent.com/77746043/221887135-14949781-fa3b-4729-b6d3-267342a2e822.png)
  
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
    }
  
  After the execution of all tests is finnished the browser is closed and the driver executable is killed. Also if there are any failed tests they are printed
  as a array of strings.
  
      @AfterAll
    static void doAfterAllTests() {
        //After all tests are finished, prints out all the failed tests
        System.out.println("************* Failed tests: *************");
        System.out.println(TestResultsWatcher.failedTests);
    }
  
  The class can also contain other method, which are commonly used among the other test classes.
  
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
    
