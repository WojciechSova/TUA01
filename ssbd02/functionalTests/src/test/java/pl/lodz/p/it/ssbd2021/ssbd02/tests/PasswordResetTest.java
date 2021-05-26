package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.*;

public class PasswordResetTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private WebDriver driver;
    private final String email = "nieistnieje@aaa.pl";
    private final String adminLogin = "admin";
    private final String actionType = "passwd";
    private final String newPassword = "password??";
    private final String currentPassword = "password?";
    private final String query = "SELECT url FROM public.one_time_url o WHERE o.action_type ='";
    private MainPage mainPage;
    private LoginPage loginPage;
    private PasswordResetPage passwordResetPage;
    private NewPasswordPage newPasswordPage;

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
    public void passwordResetTest() {
        resetPassword(newPassword);
        AdminMainPage adminMainPage = TestUtils.logInAsAdmin(driver, adminLogin, newPassword);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        Assertions.assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
        adminMainPage.logOut();
        resetPassword(currentPassword);
    }

    @Test
    public void invalidFormatErrorTest() {
        passwordResetPage = openPasswordResetForm();
        passwordResetPage.provideEmailAddress(email.substring(0, 3));
        Assertions.assertTrue(driver.findElement(passwordResetPage.getInvalidEmailFormatError()).isDisplayed());
    }

    @Test
    public void shortPasswordErrorTest() {
        passwordResetPage = openPasswordResetForm();
        passwordResetPage.provideEmailAddress(email);

        String oneTimeUrl = TestUtils.getOneTimeUrl(actionType, query);
        String url = TestUtils.url.concat("/reset/password/").concat(oneTimeUrl);
        driver.get(url);

        NewPasswordPage newPasswordPage = new NewPasswordPage(driver);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordPage.getNewPasswordResetForm()));
        newPasswordPage.setNewPassword(newPassword.substring(0, 3), newPassword.substring(0, 3));

        Assertions.assertTrue(driver.findElement(newPasswordPage.getShortPasswordError()).isDisplayed());
    }

    @Test
    public void differentPasswordsErrorTest() {
        passwordResetPage = openPasswordResetForm();
        passwordResetPage.provideEmailAddress(email);

        String oneTimeUrl = TestUtils.getOneTimeUrl(actionType, query);
        String url = TestUtils.url.concat("/reset/password/").concat(oneTimeUrl);
        driver.get(url);

        NewPasswordPage newPasswordPage = new NewPasswordPage(driver);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordPage.getNewPasswordResetForm()));
        newPasswordPage.setNewPassword(newPassword, newPassword.concat("??"));

        Assertions.assertTrue(driver.findElement(newPasswordPage.getDifferentPasswordsError()).isDisplayed());
    }

    public PasswordResetPage openPasswordResetForm() {
        mainPage = new MainPage(driver);
        loginPage = mainPage.openLoginForm();
        return loginPage.openPasswordResetPage();
    }

    public void resetPassword(String newPassword) {
        passwordResetPage = openPasswordResetForm();
        passwordResetPage.provideEmailAddress(email);

        String oneTimeUrl = TestUtils.getOneTimeUrl(actionType, query);
        String url = TestUtils.url.concat("/reset/password/").concat(oneTimeUrl);
        driver.get(url);

        newPasswordPage = new NewPasswordPage(driver);
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordPage.getNewPasswordResetForm()));
        newPasswordPage.setNewPassword(newPassword, newPassword);

        driver.get(TestUtils.url);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
