package utils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static tests.GlobalVariables.driver;
import static tests.GlobalVariables.localTempDirectory;
import static tests.GlobalVariables.operationSystem;

public class TestResultsWatcher implements TestWatcher {

    //a list storing the names of failed tests, used for creating a summary at the end of the test suite
    public static List<String> failedTests = new ArrayList<>();

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        driver.quit();
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        driver.quit();
    }

    //takes a screenshot, if a test fails, only if the currently used OS is Windows and save the file in the system temp folder
    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        if (operationSystem.contains("Windows")) {
            String screenshotFileName = localTempDirectory + extensionContext.getDisplayName() + "_" + TestDataGeneration.formatDate("hh-mm-ss_dd.MM", "bg") + ".jpeg";
            System.out.println("Test failed! Screenshot saved to: " + screenshotFileName);
            failedTests.add(extensionContext.getDisplayName());
            takeSnapShot(driver, screenshotFileName);
        }
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
