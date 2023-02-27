package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;

import static tests.GlobalVariables.driver;
import static tests.GlobalVariables.isMobile;
import static tests.cfui.ExampleBaseTestCFUI.getWait;
import static utils.TimeUtils.waitForSeconds;

public class NotificationsHandler {

    //Method used for verifying the content of email notifications,
    //if an email is not received for up to two minutes an error message is displayed
    public static void openMailinatorMailInbox(String mailinatorUser, String receivedEmail, String emailBodyText, String emailButton) {
        driver.get("https://mailinator.com/v4/public/inboxes.jsp?to=" + mailinatorUser);

        for (int i = 1; i <= 10; i++) {
            try {
                if (isMobile) {
                    getWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[contains(text(), 'just now')]"))));
                    getWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[contains(text(), '" + receivedEmail + "')]"))));
                } else {
                    getWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//td[contains(text(), 'just now')]"))));
                    getWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//td[contains(text(), '" + receivedEmail + "')]"))));
                }
                System.out.println("************* Email received: " + receivedEmail + " *************");
                break;
            } catch (Exception e) {
                System.out.println("Attempt to find mail #" + i);
                waitForSeconds(12);
            }
            if (i == 10) {
                System.out.println("************* Email was not received: " + receivedEmail + " *************");
            }
        }
        if (isMobile) {
            driver.findElement(By.xpath("//a[contains(text(), '" + receivedEmail + "')]")).click();
        } else {
            driver.findElement(By.xpath("//td[contains(text(), '" + receivedEmail + "')]")).click();
        }

        driver.switchTo().frame(driver.findElement(By.id("html_msg_body")));
        TimeUtils.waitForSeconds(2);
        getWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//td[contains(text(), '" + emailBodyText + "')]"))));
        System.out.println("************* Email content verified: " + emailBodyText + " *************");

        try {
            driver.findElement(By.xpath("//a[contains(text(), '" + emailButton + "')]")).click();
            System.out.println("************* Email button clicked *************");
            ArrayList<String> arrayList = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(arrayList.get(1));
        } catch (Exception e) {
            System.out.println("No button present in email body");
        }
    }
}
