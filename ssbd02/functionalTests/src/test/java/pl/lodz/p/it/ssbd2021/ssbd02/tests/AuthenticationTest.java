package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuthenticationTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String url = "https://studapp.it.p.lodz.pl:8402/#";
    private final String login = "admin";
    private final String incorrectLogin = "nieprawidlowe";
    private final String password = "password?";
    private final String loginFormButtonId = "loginFormBtn";
    private final String loginMainButtonId = "loginMainBtn";
    private final String currentAccessLevelId = "currentLevel";
    private final String currentUsernameId = "usernameMain";
    private final String errorMessageId = "invalidCredentialsLabel";
    private final String loginField = "login";
    private final String passwordField = "password";
    private WebDriver driver;

    @BeforeAll
    static void initAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

        options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.setHeadless(true);
    }

    @BeforeEach
    public void initEach() {
        driver = new ChromeDriver(options);
        driver.get(url);

        driverWait = new WebDriverWait(driver, 10);
    }

    @Test
    public void authenticationCompletedTest() {
        driver.findElement(By.id(loginMainButtonId))
                .click();

        driver.findElement(By.id(loginField))
                .sendKeys(login);
        driver.findElement(By.id(passwordField))
                .sendKeys(password);
        driver.findElement(By.id(loginFormButtonId))
                .click();

        driverWait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id(loginField))));

        final String currentRole = driver.findElement(By.id(currentAccessLevelId)).getText();
        final String currentLogin = driver.findElement(By.id(currentUsernameId)).getText();

        Assertions.assertEquals("administrator", currentRole.toLowerCase());
        Assertions.assertEquals("admin", currentLogin.toLowerCase());
    }

    @Test
    public void authenticationUncompletedTest() {
        driver.findElement(By.id(loginMainButtonId))
                .click();

        driver.findElement(By.id(loginField))
                .sendKeys(incorrectLogin);
        driver.findElement(By.id(passwordField))
                .sendKeys(password);
        driver.findElement(By.id(loginFormButtonId))
                .click();

        Assertions.assertTrue(driver.findElement(By.id(errorMessageId)).isDisplayed());
    }

    @AfterEach
    void end() {
        driver.close();
    }
}
