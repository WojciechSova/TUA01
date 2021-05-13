package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.LoginPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.MainPage;

import java.util.Locale;

public class AuthenticationTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String url = "https://studapp.it.p.lodz.pl:8402/#";
    private final String adminLogin = "admin";
    private final String adminPassword = "password?";
    private final String incorrectLogin = "nieprawidlowe";
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

        driverWait = new WebDriverWait(driver, 25);
    }

    @Test
    public void authenticationCompletedTest() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        AdminMainPage adminMainPage = loginPage.loginValidAdmin(adminLogin, adminPassword);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        Assertions.assertEquals("administrator", adminMainPage.getLoggedInUserAccessLevel().toLowerCase(Locale.ROOT));
        Assertions.assertEquals("admin", adminMainPage.getLoggedInUserLogin().toLowerCase(Locale.ROOT));
    }

    @Test
    public void authenticationUncompletedTest() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        AdminMainPage adminMainPage = loginPage.loginValidAdmin(incorrectLogin, adminPassword);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getErrorMessageId()));
        Assertions.assertTrue(adminMainPage.isLoginErrorMessageDisplayed());
    }

    @AfterEach
    void end() {
        driver.close();
    }
}
