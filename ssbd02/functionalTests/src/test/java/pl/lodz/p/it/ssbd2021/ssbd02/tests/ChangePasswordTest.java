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
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountDetailsPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.ChangePasswordPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.MainPage;

import static org.junit.jupiter.api.Assertions.*;

public class ChangePasswordTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String adminLogin = "admin";
    private final String adminPassword = "password?";
    private final String newPassword = "password??";
    private WebDriver driver;
    private MainPage mainPage;
    private AdminMainPage adminMainPage;
    private ChangePasswordPage changePasswordPage;

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
    public void changePasswordTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changePassword(adminPassword, newPassword, newPassword);

        mainPage = adminMainPage.logOut();

        adminMainPage = TestUtils.logInAsAdmin(driver, adminLogin, newPassword);
        changePassword(newPassword, adminPassword, adminPassword);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
    }

    @Test
    public void incorrectPasswordErrorTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changePassword(newPassword, newPassword, newPassword);

        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        driver.findElement(changePasswordPage.getOldPassword()).sendKeys(Keys.SHIFT);
        assertTrue(driver.findElement(changePasswordPage.getInvalidPasswordError()).isDisplayed());
    }

    @Test
    public void samePasswordErrorTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changePassword(adminPassword, adminPassword, adminPassword);

        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        driver.findElement(changePasswordPage.getOldPassword()).sendKeys(Keys.SHIFT);
        assertTrue(driver.findElement(changePasswordPage.getSamePasswordError()).isDisplayed());
    }

    @Test
    public void differentPasswordsErrorTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changePassword(adminPassword, newPassword, newPassword.concat("1234"));
        driver.findElement(changePasswordPage.getOldPassword()).sendKeys(Keys.SHIFT);

        assertFalse(driver.findElement(changePasswordPage.getConfirm()).isEnabled());
        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        assertTrue(driver.findElement(changePasswordPage.getDifferentPasswordsError()).isDisplayed());
    }

    @Test
    public void shortPasswordErrorTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changePassword(adminPassword, newPassword.substring(0, 5), newPassword.substring(0, 5));

        assertTrue(driver.findElement(changePasswordPage.getForm()).isDisplayed());
        assertTrue(driver.findElement(changePasswordPage.getShortPasswordError()).isDisplayed());
    }

    private void changePassword(String currentPassword, String newPassword, String newPasswordRepeat) {
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        AccountDetailsPage accountDetailsPage = adminMainPage.openOwnAccountDetails();

        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/account")));
        driverWait.until(ExpectedConditions.presenceOfElementLocated(accountDetailsPage.getChangePasswordBtn()));
        changePasswordPage = accountDetailsPage.openChangePasswordPage();

        driverWait.until(ExpectedConditions.presenceOfElementLocated(changePasswordPage.getForm()));
        changePasswordPage.changePassword(currentPassword, newPassword, newPasswordRepeat);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
