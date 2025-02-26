package StepDef;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import pages.Cart;
import pages.Checkout;
import pages.Login;
import pages.Menu;
import utils.DriverManager;
import utils.ScreenshotUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.openqa.selenium.By.xpath;

public class checkoutStepDef {
    AndroidDriver driver;
    static Process appiumProcess;
    ScreenshotUtil ss = new ScreenshotUtil(driver);

    @Given("User use Device {string} and Open the Apps Saucelabs")
    public void userUseDeviceAndOpenTheAppsSaucelabs(String device) throws InterruptedException, MalformedURLException {
        openCommandPromptWithAppium();

        UiAutomator2Options options = getDeviceOptions(device);
        System.out.println("Selected device options: " + options.toString());

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        DriverManager.setDriver(driver);
        Thread.sleep(2000);
        ss.takeScreenshotWithResizedHeight("Open Apps");
    }

    @And("User input username {string}, password {string} and Click Login")
    public void userInputUsernamePasswordAndClickLogin(String user, String pass) throws InterruptedException {
        Login login = new Login(driver);
        login.login(user, pass);
    }

    @And("User click toggle")
    public void userClickToggle() throws InterruptedException {
        Cart cart = new Cart(driver);
        cart.toggle();
    }

    @And("User click sort {string}")
    public void userClickSort(String sort) throws InterruptedException {
        Cart cart = new Cart(driver);
        cart.sort(sort);
    }

    @When("User click on a product")
    public void userClickOnAProduct() throws InterruptedException {
        // Localiza e clica no produto "Sauce Labs Backpack"
        WebElement product = driver.findElement(
                By.xpath("//android.widget.TextView[@content-desc='test-Item title' and @text='Sauce Labs Backpack']"));
        product.click();
        Thread.sleep(2000); // Pequeno delay para garantir que a navegação foi concluída
    }

    @Then("the wrong product is shown")
    public void theWrongProductIsShown() {
        // Localiza a imagem do produto na tela de detalhes
        WebElement productImage = driver.findElement(
                By.xpath("//android.view.ViewGroup[@content-desc='test-Image Container']/android.widget.ImageView"));

        // Apenas valida que o elemento da imagem está presente na tela
        if (productImage.isDisplayed()) {
            System.out.println("Imagem do produto foi exibida corretamente.");
        } else {
            System.out.println("Erro: A imagem do produto não foi encontrada.");
        }
    }

    @When("User add cart product {string}")
    public void userAddCartProduct(String product) throws InterruptedException {
        Cart cart = new Cart(driver);
        cart.addToCart(product);
    }

    @When("User drag and drop to add cart product {string}")
    public void userDragAndDropAddCartProduct(String product) throws InterruptedException {
        Cart cart = new Cart(driver);
        cart.dragAndDropAddToCart(product);
    }

    @When("User Click Menu")
    public void userClickMenu() throws InterruptedException {
        Menu menu = new Menu(driver);
        menu.clickMenu();
    }

    @And("User Click Drawing")
    public void userClickDrawing() {
        Menu menu = new Menu(driver);
        menu.clickDrawing();
    }

    @And("User checkout the cart")
    public void userCheckoutTheCart() throws InterruptedException {
        Checkout co = new Checkout(driver);
        co.checkout();
    }

    @Then("Checkout Complete")
    public void checkoutComplete() throws InterruptedException {
        WebElement complete = driver.findElement(xpath("//android.widget.TextView[@text=\"CHECKOUT: COMPLETE!\"]"));
        complete.isDisplayed();
        System.out.println(complete.getText());
        ss.takeScreenshotWithResizedHeight("Checkout Complete");
        driver.findElement(AppiumBy.accessibilityId("test-BACK HOME")).click();
        Thread.sleep(2000);
    }

    @Then("the error message {string} is displayed")
    public void theErrorMessageIsDisplayed(String expectedMessage) throws InterruptedException {
        WebElement errorMessageElement = driver
                .findElement(AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Sorry')]"));

        String actualMessage = errorMessageElement.getText();
        System.out.println("Actual error message: " + actualMessage);

        if (!actualMessage.equals(expectedMessage)) {
            throw new AssertionError("Expected: " + expectedMessage + " but found: " + actualMessage);
        }

        ss.takeScreenshotWithResizedHeight("Error Message Displayed");
    }

    public static UiAutomator2Options emulator() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("emulator-5554");
        // options.setApp(System.getProperty("user.dir")+"/src/test/resources/apps/AndroidSauceLabsMobile.apk");
        options.setAppPackage("com.swaglabsmobileapp");
        options.setAppActivity("com.swaglabsmobileapp.SplashActivity");
        options.setNewCommandTimeout(Duration.ofMillis(300));

        return options;
    }

    public static UiAutomator2Options oppoF9() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("RSFUNNSGMBW4U8GE");
        // options.setApp(System.getProperty("user.dir")+"/src/test/resources/apps/AndroidSauceLabsMobile.apk");
        options.setAppPackage("com.swaglabsmobileapp");
        options.setAppActivity("com.swaglabsmobileapp.SplashActivity");
        options.setNewCommandTimeout(Duration.ofMillis(300));

        return options;
    }

    public static UiAutomator2Options getDeviceOptions(String deviceName) {
        switch (deviceName.toLowerCase()) {
            case "emulator":
                return emulator();
            default:
                throw new IllegalArgumentException("Device name not recognized: " + deviceName);
        }
    }

    public static void openCommandPromptWithAppium() throws InterruptedException {
        try {
            if (isAppiumRunning()) {
                Runtime.getRuntime().exec("cmd.exe /c appium --allow-cors");
                System.out.println("Appium --allow-cors command executed in existing Command Prompt.");
            } else {
                Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /k appium --allow-cors");
                System.out.println("Command Prompt opened and Appium server started with --allow-cors.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(5000);
    }

    private static boolean isAppiumRunning() {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("node.exe")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
