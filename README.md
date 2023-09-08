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

  The browser is opened in headless mode (no UI) in incognito mode, that way the testing environment will remain the same each time a test is run.
  Next the setEmailAndNotificationsContent() method is called. This function is responsible for managing the content of the notifications throughout the     website
  (onsite, email and pop-ups)
  Next readCSVFileAndSaveContentAsListOfStrings() method is called, which reads a specific csv file and saves its content to a list of Strings.
