package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import io.cucumber.java.Scenario;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;

public class utils {
    public static AndroidDriver driver;
    public Scenario scenario;
    static ScreenshotUtil ss = new ScreenshotUtil(driver);

    public void findingElementAndClick(String accessibilityId, String name) throws InterruptedException {
        WebElement findElement = null;
        boolean find = false;
        ss.takeScreenshotWithResizedHeight(name);

        int maxTrial = 10; 
        int trial = 0;

        while (trial < maxTrial && !find) {

            try {
                findElement = driver.findElement(AppiumBy.accessibilityId(accessibilityId));
                find = true; 
                ss.takeScreenshotWithResizedHeight(name);
            } catch (NoSuchElementException e) {
                swipeUp();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            }
            trial++;
        }
        findElement.click();
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

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
    }

    public static void swipeUpALitte() {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();

        int startX = width / 2;
        int startY = (int) (height * 0.85); 
        int endY = (int) (height * 0.70);   

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

    public static void swipeToDraw(By locator,double percentageStartX, double percentageEndX, double percentageStartY, double percentageEndY) {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();

        int heightElement = driver.findElement(locator).getSize().getHeight();
        int widthElement = driver.findElement(locator).getSize().getWidth();

        int xElement = driver.findElement(locator).getLocation().getX();
        int yElement = driver.findElement(locator).getLocation().getY();

        int startX = (int) (xElement+(widthElement * percentageStartX));
        int endX = (int) (xElement+(widthElement * percentageEndX));
        int startY = (int) (yElement+(heightElement * percentageStartY));
        int endY = (int) (yElement+(heightElement * percentageEndY));

        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var start = new Point(startX, startY);
        var end = new Point(endX, endY);
        var swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), start.getX(), start.getY()));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), end.getX(), end.getY()));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
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
                if(driver.findElement(locator).isDisplayed()){
                    ss.takeScreenshotWithResizedHeight("Find Product");
                    waitForElementVisibleWithRetry(locator, 5, 5);
                }
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

    public static boolean waitForElementVisibleWithRetry(By locator, int timeoutOfSeconds, int retryAttempts) {
        int attempts = 0;
        while (attempts < retryAttempts) {
            try {
                System.out.println("Waiting for element to be visible, attempt " + (attempts + 1));
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutOfSeconds));
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                System.out.println("Element is visible.");
                return true;
            } catch (Exception e) {
                System.out.println("Error occurred while waiting for element to be visible on attempt " + (attempts + 1) + ": " + e.getMessage());
            }
            attempts++;
        }
        System.out.println("Element not found after " + retryAttempts + " attempts.");
        return false;
    }


}
