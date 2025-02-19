import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.By.xpath;

public class simpleTest {
    static AndroidDriver driver;
    static Process appiumProcess;

    public static UiAutomator2Options emulator(){
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("emulator-5554");
        options.setApp(System.getProperty("user.dir")+"/src/test/resources/apps/AndroidSauceLabsMobile.apk");
        options.setAppPackage("com.swaglabsmobileapp");
        options.setAppActivity("com.swaglabsmobileapp.SplashActivity");
        options.setNewCommandTimeout(Duration.ofMillis(300));

        return options;
    }

    @Test
    public void androidLaunchTest() throws InterruptedException, MalformedURLException {
        openCommandPromptWithAppium();

        UiAutomator2Options options = emulator();

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

        login("standard_user", "secret_sauce");

        addToCart("Sauce Labs Fleece Jacket");
        addToCart("Sauce Labs Onesie");
        addToCart("Sauce Labs Backpack");
        addToCart("Sauce Labs Bolt T-Shirt");
        addToCart("Sauce Labs Bike Light");
        addToCart("Test.allTheThings() T-Shirt (Red)");

        checkout();

        driver.quit();
        stopAppium();
    }

    public static void openCommandPromptWithAppium() throws InterruptedException {
        try {
            Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /k appium --allow-cors");
            System.out.println("Command Prompt opened and Appium server started with --allow-cors.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(5000);
    }

    public void stopAppium() {
        if (appiumProcess != null) {
            appiumProcess.destroy();
            System.out.println("Appium server stopped.");
        } else {
            System.out.println("No Appium process is running.");
        }
        closeCommandPrompt();
    }

    public void closeCommandPrompt() {
        try {
            Runtime.getRuntime().exec("taskkill /IM cmd.exe /F");
            System.out.println("Command Prompt closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void swipeUp() {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();

        int startX = width / 2;
        int startY = (int) (height * 0.85); 
        int endY = (int) (height * 0.25);   

        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var start = new Point(startX, startY);
        var end = new Point(startX, endY);
        var swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), start.getX(), start.getY()));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), end.getX(), end.getY()));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }

    public static void swipeDown() {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();

        int startX = width / 2;
        int startY = (int) (height * 0.25); 
        int endY = (int) (height * 0.85); 

        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var start = new Point(startX, startY);
        var end = new Point(startX, endY);
        var swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), start.getX(), start.getY()));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), end.getX(), end.getY()));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }

    public void login(String username, String password) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("test-Username")).sendKeys(username);
        driver.findElement(AppiumBy.accessibilityId("test-Password")).sendKeys(password);
        driver.findElement(AppiumBy.accessibilityId("test-LOGIN")).click();
    }

    public void addToCart(String Product) throws InterruptedException {
        Thread.sleep(2000);

        findElementWithSwipe(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + Product + "\")"));
        Thread.sleep(2000);
        swipeUp();
        driver.findElement(AppiumBy.accessibilityId("test-ADD TO CART")).click();
        driver.findElement(AppiumBy.accessibilityId("test-BACK TO PRODUCTS")).click();
    }

    public void checkout() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup/android.widget.ImageView")).click();
        Thread.sleep(3000);

        findingElementAndClick("test-CHECKOUT");
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("test-First Name")).sendKeys("Thiago");
        driver.findElement(AppiumBy.accessibilityId("test-Last Name")).sendKeys("Goncalves");
        driver.findElement(AppiumBy.accessibilityId("test-Zip/Postal Code")).sendKeys("12130");
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("test-CONTINUE")).click();
        Thread.sleep(2000);

        findingElementAndClick("test-FINISH");
        Thread.sleep(7000);
        WebElement complete = driver.findElement(xpath("//android.widget.TextView[@text=\"CHECKOUT: COMPLETE!\"]"));
        complete.isDisplayed();
        System.out.println(complete.getText());
        driver.findElement(AppiumBy.accessibilityId("test-BACK HOME")).click();
        Thread.sleep(2000);
    }

    public void findingElementAndClick(String accessibilityId) throws InterruptedException {
        WebElement findElement = null;
        boolean find = false;
        int maxTrial = 10;
        int trial = 0;
        while (trial < maxTrial && !find) {
            try {
                findElement = driver.findElement(AppiumBy.accessibilityId(accessibilityId));
                find = true;
            } catch (NoSuchElementException e) {
                swipeUp();
                Thread.sleep(2000);

            }
            trial++; 
        }
        findElement.click();
    }

    public static WebElement waitToVisible(AndroidDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return (WebElement) wait.until(ExpectedConditions.visibilityOf(element));

    }

    public static WebElement waitToClickable(AndroidDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return (WebElement) wait.until(ExpectedConditions.elementToBeClickable(element));

    }

    public static WebElement waitToPresence(AndroidDriver driver, AppiumBy element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(element));

    }

    public static boolean isAtTop() {
        try {
            swipeDown();
            Thread.sleep(1000);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isAtBottom() {
        try {
            swipeUp();
            Thread.sleep(1000);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void findElementWithSwipe(By locator) throws InterruptedException {
        int swipeUpTrials = 3;
        int swipeDownTrials = 3;
        boolean elementFound = false;
        int totalTrials = 0;

        boolean startAtTop = isAtTop();
        boolean startAtBottom = isAtBottom();

        while (!elementFound && totalTrials < (swipeUpTrials + swipeDownTrials)) {
            try {
                driver.findElement(locator).click();
                elementFound = true;
            } catch (NoSuchElementException e) {
                if (startAtTop && totalTrials < swipeUpTrials) {
                    swipeUp();
                } else if (startAtBottom && totalTrials < swipeDownTrials) {
                    swipeDown();
                } else if (totalTrials < swipeUpTrials) {
                    swipeUp();
                } else {
                    swipeDown();
                }
                Thread.sleep(2000);
            }
            totalTrials++;
        }

        if (!elementFound) {
            System.out.println("Element not found after 6 swipes (3 up and 3 down).");
        }
    }
}
