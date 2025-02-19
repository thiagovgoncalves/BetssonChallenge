package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import utils.utils;
import utils.ScreenshotUtil;

import java.time.Duration;

import static org.openqa.selenium.By.xpath;


public class Checkout extends utils{
    public Checkout(AndroidDriver driver){this.driver = driver;}

    public void checkout() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup/android.widget.ImageView")).click();
        waitForElementVisibleWithRetry(AppiumBy.xpath("//android.widget.TextView[@text=\"YOUR CART\"]"),5,5);
        findingElementAndClick("test-CHECKOUT", "Cart List");
        waitForElementVisibleWithRetry(AppiumBy.accessibilityId("test-First Name"),5,5);
        WebElement firstname = driver.findElement(AppiumBy.accessibilityId("test-First Name"));
        firstname.sendKeys("Thiago");
        String textFirstName = firstname.getText();
        Assert.assertEquals("Thiago",textFirstName);

        WebElement lastname = driver.findElement(AppiumBy.accessibilityId("test-Last Name"));
        lastname.sendKeys("Goncalves");
        String textLastName = lastname.getText();
        Assert.assertEquals("Goncalves",textLastName);

        WebElement zipcode = driver.findElement(AppiumBy.accessibilityId("test-Zip/Postal Code"));
        zipcode.sendKeys("12130");
        String textZipCode = zipcode.getText();
        Assert.assertEquals("12130", textZipCode);

        Assert.assertEquals("Thiago",firstname.getText());
        Assert.assertEquals("Goncalves",lastname.getText());
        Assert.assertEquals("12130",zipcode.getText());

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        ScreenshotUtil ss = new ScreenshotUtil(driver);
        ss.takeScreenshotWithResizedHeight("Data Information");
        driver.findElement(AppiumBy.accessibilityId("test-CONTINUE")).click();
        waitForElementVisibleWithRetry(AppiumBy.xpath("//android.widget.TextView[@text=\"CHECKOUT: OVERVIEW\"]"),5,5);
        findingElementAndClick("test-FINISH","Checkout Overview");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));

    }


}
