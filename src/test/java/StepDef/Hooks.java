package StepDef;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.DriverManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import utils.ScreenshotUtil;

import static StepDef.checkoutStepDef.appiumProcess;

public class Hooks {
    AndroidDriver driver;
    private static Scenario currentScenario;

    @Before
    public void setUp(Scenario scenario) {
        currentScenario = scenario; 
    }

    public static Scenario getCurrentScenario() {
        return currentScenario;
    }


    @After
    public void takeScreenshotFinal(Scenario scenario) {
        try {
            AndroidDriver driver = DriverManager.getDriver();
            if (driver != null) {
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                BufferedImage originalImage = ImageIO.read(screenshotFile);

                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();

                int newHeight = 700;
                int newWidth = (int) ((double) originalWidth / originalHeight * newHeight);

                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
                Graphics2D g2d = resizedImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                g2d.dispose();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "png", baos);
                byte[] resizedScreenshot = baos.toByteArray();

                if (scenario.isFailed()) {
                    scenario.attach(resizedScreenshot, "image/png", "Failed Screenshot");
                    System.out.println("Screenshot taken for failed scenario: " + scenario.getName());
                } else {
                    scenario.attach(resizedScreenshot, "image/png", "Passed Screenshot");
                    System.out.println("Screenshot taken for passed scenario: " + scenario.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After(order = 1)
    public void tearDown() throws Exception {
        AndroidDriver driver = DriverManager.getDriver();
        if (driver != null) {
            driver.quit();
            DriverManager.quitDriver();
        }
    }

    public void stopAppium() throws AWTException, InterruptedException {
        try {
            System.out.println("Stopping Appium...");
            if (appiumProcess != null) {
                appiumProcess.destroy();
            }
            Runtime.getRuntime().exec("taskkill /IM node.exe /F");
            System.out.println("Appium Server Stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
