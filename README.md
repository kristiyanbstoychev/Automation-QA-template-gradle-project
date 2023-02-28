# Automation-QA-template-gradle-project
A template used for creating Automation QA projects, using gradle
  
  ## Architecture
  The project is based on the Page object model. Tests and locators are created in separate classes.
  The PAGES package contains the locators and methods needed for testing a certain page. Example: LoginPage
  The TESTS package contains sets of tests that verify certain functionalities. Example LoginTests
  In each of theese packages there is a Base class (Page or Test), that contains commonly used code between the rest of the tests or pages.
  
  ### BaseTest class breakdown:
  ### Tests setup:
  Before the start of each test, the pickBrowser method is called. The method is repsonsible for determening the browser and device used for testing.
  In order for the tests to be easily implemented with a CI/CD environment a set of globally set variables is used (BROWSER_FOR_TESTING and DEVICE_FOR_TESTING).
  Default values are set for both variables.
  After the browser and device for testing are determined a set of certaing browser capabillities are applied.
  The browser is opened in headless mode (no UI) in incognito mode, that way the testing environment will remain the same each time a test is run.
  Next the setEmailAndNotificationsContent() method is called. This function is responsible for managing the content of the notifications throughout the website
  (onsite, email and pop-ups)
  Next readCSVFileAndSaveContentAsListOfStrings() method is called, which reads a specific csv file and saves its content to a list of Strings.
  After the execution of all tests is finnished the browser is closed and the driver executable is killed. Also if there are any failed tests they are printed
  as a array of strings.
  
  BasePage class breakdown:
  
