package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;

import static tests.GlobalVariables.driver;
import static tests.GlobalVariables.isMobile;
import static tests.ExampleBaseTest.getWait;
import static utils.TimeUtils.waitForSeconds;

public class NotificationsHandler {

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
}
