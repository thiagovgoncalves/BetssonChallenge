package utils;

import StepDef.Hooks;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {
    public AndroidDriver driver;

    public ScreenshotUtil(AndroidDriver driver) {
        this.driver = driver;
    }

    public void takeScreenshotWithResizedHeight(String name) {
        try {
            Scenario scenario = Hooks.getCurrentScenario();
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

                scenario.attach(resizedScreenshot, "image/png", name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
