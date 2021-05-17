package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChangePasswordTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String adminLogin = "admin";
    private final String adminPassword = "password?";
    private final String newPassword = "password??";
    MainPage mainPage;
    LoginPage loginPage;
    AdminMainPage adminMainPage;
    ChangePasswordPage changePasswordPage;
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
        driver.get(TestUtils.url);
        driverWait = new WebDriverWait(driver, 25);
    }

    @Test
    public void changePasswordTest() {
        logIn(adminPassword);
        changePassword(adminPassword, newPassword, newPassword);

        mainPage = adminMainPage.logOut();

        logIn(newPassword);
        changePassword(newPassword, adminPassword, adminPassword);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
    }

    @Test
    public void incorrectPasswordErrorTest() {
        logIn(adminPassword);
        changePassword(newPassword, newPassword, newPassword);

        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        assertTrue(driver.findElement(changePasswordPage.getInvalidPasswordError()).isDisplayed());
    }

    @Test
    public void samePasswordErrorTest() {
        logIn(adminPassword);
        changePassword(adminPassword, adminPassword, adminPassword);

        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        assertTrue(driver.findElement(changePasswordPage.getSamePasswordError()).isDisplayed());
    }

    @Test
    public void differentPasswordsErrorTest() {
        logIn(adminPassword);
        changePassword(adminPassword, newPassword, newPassword.concat("1234"));
        driver.findElement(changePasswordPage.getOldPassword()).sendKeys(Keys.SHIFT);

        assertFalse(driver.findElement(changePasswordPage.getConfirm()).isEnabled());
        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        assertTrue(driver.findElement(changePasswordPage.getDifferentPasswordsError()).isDisplayed());
    }

    @Test
    public void shortPasswordErrorTest() {
        logIn(adminPassword);
        changePassword(adminPassword, newPassword.substring(0, 5), newPassword.substring(0, 5));

        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        assertTrue(driver.findElement(changePasswordPage.getShortPasswordError()).isDisplayed());
    }

    private void logIn(String password) {
        mainPage = new MainPage(driver);
        loginPage = mainPage.openLoginForm();
        adminMainPage = loginPage.loginValidAdmin(adminLogin, password);
    }

    private void changePassword(String currentPassword, String newPassword, String newPasswordRepeat) {
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        AccountDetailsPage accountDetailsPage = adminMainPage.openAccountDetails();

        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/account")));
        changePasswordPage = accountDetailsPage.openChangePasswordPage();

        driverWait.until(ExpectedConditions.presenceOfElementLocated(changePasswordPage.getForm()));
        changePasswordPage.changePassword(currentPassword, newPassword, newPasswordRepeat);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
