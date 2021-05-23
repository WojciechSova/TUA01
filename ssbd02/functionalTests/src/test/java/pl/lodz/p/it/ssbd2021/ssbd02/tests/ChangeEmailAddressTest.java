package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.TestDatabaseConnection;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountDetailsPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountsListPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.ChangeEmailPage;

import java.util.List;

public class ChangeEmailAddressTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private WebDriver driver;
    private final String currentEmail = "nieistnieje@aaa.pl";
    private final String newEmail = "newEmail@newEmail.pl";
    private String userLogin;
    private AdminMainPage adminMainPage;
    private AccountDetailsPage accountDetailsPage;
    private AccountsListPage accountsListPage;
    private ChangeEmailPage changeEmailPage;

    @BeforeAll
    static void initAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        options.addArguments("−−lang=pl");
//        options.setHeadless(true);
    }

    @BeforeEach
    public void initEach() {
        driver = new ChromeDriver(options);
        driver.get(TestUtils.url);
        driverWait = new WebDriverWait(driver, 25);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void changeEmailAddressTest(boolean ownProfile) {
        String oneTimeUrl;
        String url;

        if (ownProfile) {
            logInAndOpenAccountDetails();
        } else {
            logInAndOpenAnotherUserAccountDetails();
        }

        changeEmail(newEmail, newEmail);
        driverWait.until(ExpectedConditions.elementToBeClickable(changeEmailPage.getConfirmButton()));
        driver.findElement(changeEmailPage.getConfirmButton()).click();
        oneTimeUrl = TestDatabaseConnection.getOneTimeUrl(newEmail);
        url = TestUtils.url.concat("/confirm/email/" + oneTimeUrl);
        driver.get(url);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void invalidFormatErrorTest(boolean ownProfile) {
        if (ownProfile) {
            logInAndOpenAccountDetails();
        } else {
            logInAndOpenAnotherUserAccountDetails();
        }

        changeEmail(newEmail.substring(0, 7), newEmail);
        Assertions.assertFalse(driver.findElement(changeEmailPage.getConfirmButton()).isEnabled());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getChangeEmailForm()).isDisplayed());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getInvalidFormatError()).isDisplayed());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getDifferentPasswordsError()).isDisplayed());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void differentPasswordsErrorTest(boolean ownProfile) {
        if (ownProfile) {
            logInAndOpenAccountDetails();
        } else {
            logInAndOpenAnotherUserAccountDetails();
        }

        changeEmail(newEmail, newEmail.substring(3));
        Assertions.assertFalse(driver.findElement(changeEmailPage.getConfirmButton()).isEnabled());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getChangeEmailForm()).isDisplayed());
        Assertions.assertTrue(driver.findElement(changeEmailPage.getDifferentPasswordsError()).isDisplayed());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void existingEmailErrorTest(boolean ownProfile) {
        //TODO exisitng e-mail error check
    }

    private void logInAndOpenAccountDetails() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        accountDetailsPage = adminMainPage.openAccountDetails();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(TestUtils.adminLogin)));
    }

    private void logInAndOpenAnotherUserAccountDetails() {
        adminMainPage = TestUtils.logInAsAdmin(driver);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));
        accountsListPage = adminMainPage.openAccountsList();
        driver.navigate().refresh();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts")));

        List<WebElement> tableData = accountsListPage.getTableContent();
        userLogin = tableData.get(0).getText();
        accountDetailsPage = accountsListPage.openAnotherUserAccountDetails(tableData.get(6));
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(userLogin)));
    }

    public void changeEmail(String newEmail, String newEmailRepeat) {
        changeEmailPage = accountDetailsPage.openChangeEmailPage();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(changeEmailPage.getChangeEmailForm()));
        changeEmailPage.changeEmail(newEmail, newEmailRepeat);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
