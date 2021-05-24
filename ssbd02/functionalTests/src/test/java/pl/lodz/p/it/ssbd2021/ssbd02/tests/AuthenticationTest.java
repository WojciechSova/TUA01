package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;

import java.util.Locale;

public class AuthenticationTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String password = "password?";
    private final String incorrectLogin = "nieprawidlowe";
    private WebDriver driver;

    @BeforeAll
    static void initAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("−−lang=pl");
        options.setHeadless(true);
    }

    @BeforeEach
    public void initEach() {
        driver = new ChromeDriver(options);
        driver.get(TestUtils.url);
        driverWait = new WebDriverWait(driver, 25);
    }

    @Test
    public void authenticationCompletedTest() {
        AdminMainPage adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        Assertions.assertEquals("administrator", adminMainPage.getLoggedInUserAccessLevel().toLowerCase(Locale.ROOT));
        Assertions.assertEquals("admin", adminMainPage.getLoggedInUserLogin().toLowerCase(Locale.ROOT));
    }

    @Test
    public void authenticationUncompletedTest() {
        AdminMainPage adminMainPage = TestUtils.logInAsAdmin(driver, incorrectLogin, password);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getErrorMessageId()));
        Assertions.assertTrue(adminMainPage.isLoginErrorMessageDisplayed());
    }

    @AfterEach
    void end() {
        driver.close();
    }
}
