package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountDetailsPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.ChangeEmailPage;

public class ChangeEmailAddressTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private WebDriver driver;
    private final String newEmail = "newEmail@costam.com";
    private AdminMainPage adminMainPage;
    private ChangeEmailPage changeEmailPage;


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
    public void changeEmailAddressTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changeEmail(newEmail, newEmail);
        Assertions.assertTrue(driver.findElement(changeEmailPage.getConfirmButton()).isEnabled());
    }

    @Test
    public void invalidFormatErrorTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changeEmail(newEmail.substring(0, 7), newEmail);
        Assertions.assertFalse(driver.findElement(changeEmailPage.getConfirmButton()).isEnabled());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getChangeEmailForm()).isDisplayed());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getInvalidFormatError()).isDisplayed());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getDifferentPasswordsError()).isDisplayed());
    }

    @Test
    public void differentPasswordsErrorTest() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        changeEmail(newEmail, newEmail.substring(3));
        Assertions.assertFalse(driver.findElement(changeEmailPage.getConfirmButton()).isEnabled());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getChangeEmailForm()).isDisplayed());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getDifferentPasswordsError()).isDisplayed());
    }

    public void changeEmail(String newEmail, String newEmailRepeat) {
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        AccountDetailsPage accountDetailsPage = adminMainPage.openAccountDetails();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/account")));
        changeEmailPage = accountDetailsPage.openChangeEmailPage();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(changeEmailPage.getChangeEmailForm()));
        changeEmailPage.changeEmail(newEmail, newEmailRepeat);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
