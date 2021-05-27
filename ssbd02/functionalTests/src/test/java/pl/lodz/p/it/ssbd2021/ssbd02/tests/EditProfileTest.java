package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountDetailsPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountsListPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.EditUserProfilePage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditProfileTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String newFirstName = "noweImie";
    private final String newLastName = "noweNazwisko";
    private final String newPhoneNumber = "48000000000";
    private final String existingPhoneNumber = "48123456789";
    private WebDriver driver;
    private String userLogin;
    private AccountDetailsPage accountDetailsPage;
    private EditUserProfilePage editUserProfilePage;
    private AdminMainPage adminMainPage;

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
        driverWait = new WebDriverWait(driver, 5);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void editAnotherUserProfileTest(boolean ownProfile) {
        String currentLogin;

        if (ownProfile) {
            logInAndOpenOwnDetails();
            currentLogin = TestUtils.adminLogin;
        } else {
            logInAndOpenAnotherUserDetails();
            currentLogin = userLogin;
        }

        String firstName = driver.findElement(accountDetailsPage.getFirstNameField()).getText();
        String lastName = driver.findElement(accountDetailsPage.getLastNameField()).getText();
        String phoneNumber = driver.findElement(accountDetailsPage.getPhoneNumberField()).getText();

        editData(newFirstName, newLastName, newPhoneNumber);

        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(currentLogin)));

        By editedFirstName = accountDetailsPage.getFirstNameField();
        By editedLastName = accountDetailsPage.getLastNameField();
        By editedPhoneNumber = accountDetailsPage.getPhoneNumberField();

        assertAll(
                () -> assertEquals(newFirstName, driver.findElement(editedFirstName).getText()),
                () -> assertEquals(newLastName, driver.findElement(editedLastName).getText()),
                () -> assertEquals(newPhoneNumber, driver.findElement(editedPhoneNumber).getText())
        );

        editData(firstName, lastName, phoneNumber);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void invalidPhoneNumberErrorTest(boolean ownProfile) {
        if (ownProfile) {
            logInAndOpenOwnDetails();
        } else {
            logInAndOpenAnotherUserDetails();
        }

        editData(newFirstName, newLastName, newPhoneNumber.substring(0, 1));
        driver.findElement(editUserProfilePage.getFirstNameEdit()).sendKeys(Keys.SHIFT);
        driver.findElement(editUserProfilePage.getLastNameEdit()).sendKeys(Keys.SHIFT);
        assertTrue(driver.findElement(editUserProfilePage.getInvalidNumberError()).isDisplayed());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void existingPhoneNumberErrorTest(boolean ownProfile) {
        if (ownProfile) {
            logInAndOpenOwnDetails();
        } else {
            logInAndOpenAnotherUserDetails();
        }

        editData(newFirstName, newLastName, existingPhoneNumber);
        driver.findElement(editUserProfilePage.getFirstNameEdit()).sendKeys(Keys.SHIFT);
        driver.findElement(editUserProfilePage.getLastNameEdit()).sendKeys(Keys.SHIFT);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(editUserProfilePage.getExistingNumberError()));
        assertTrue(driver.findElement(editUserProfilePage.getExistingNumberError()).isDisplayed());
    }

    private void logInAndOpenAnotherUserDetails() {
        adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        AccountsListPage accountsListPage = adminMainPage.openAccountsList();

        driver.navigate().refresh();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts")));

        List<WebElement> tableData = accountsListPage.getTableContent();
        userLogin = tableData.get(0).getText();
        accountDetailsPage = accountsListPage.openAnotherUserAccountDetails(tableData.get(6));

        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(userLogin)));
    }

    private void logInAndOpenOwnDetails() {
        adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        accountDetailsPage = adminMainPage.openOwnAccountDetails();

        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(TestUtils.adminLogin)));
    }

    private void editData(String firstName, String lastName, String phoneNumber) {
        editUserProfilePage = accountDetailsPage.openEditUserProfilePage();
        accountDetailsPage = editUserProfilePage.editUser(firstName, lastName, phoneNumber);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
