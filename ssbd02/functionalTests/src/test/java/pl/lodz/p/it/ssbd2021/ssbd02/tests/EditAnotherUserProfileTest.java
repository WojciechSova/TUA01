package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountsListPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.EditUserProfilePage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.ProfileDetailsPage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EditAnotherUserProfileTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String newFirstName = "noweImie";
    private final String newLastName = "noweNazwisko";
    private final String newPhoneNumber = "48000000000";
    private final String existingPhoneNumber = "48123456789";
    private WebDriver driver;
    private String userLogin;
    private ProfileDetailsPage profileDetailsPage;
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
        driverWait = new WebDriverWait(driver, 25);
    }

    @Test
    public void editUserTest() {
        logInAndOpenDetails();

        String firstName = driver.findElement(profileDetailsPage.getFirstNameField()).getText();
        String lastName = driver.findElement(profileDetailsPage.getLastNameField()).getText();
        String phoneNumber = driver.findElement(profileDetailsPage.getPhoneNumberField()).getText();

        editData(newFirstName, newLastName, newPhoneNumber);

        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(userLogin)));

        By editedFirstName = profileDetailsPage.getFirstNameField();
        By editedLastName = profileDetailsPage.getLastNameField();
        By editedPhoneNumber = profileDetailsPage.getPhoneNumberField();

        assertAll(
                () -> assertEquals(newFirstName, driver.findElement(editedFirstName).getText()),
                () -> assertEquals(newLastName, driver.findElement(editedLastName).getText()),
                () -> assertEquals(newPhoneNumber, driver.findElement(editedPhoneNumber).getText())
        );

        editData(firstName, lastName, phoneNumber);
    }

    @Test
    public void invalidPhoneNumberErrorTest() {
        logInAndOpenDetails();

        editData(newFirstName, newLastName, newPhoneNumber.substring(0, 5));
        driver.findElement(editUserProfilePage.getFirstNameEdit()).sendKeys(Keys.SHIFT);
        assertTrue(driver.findElement(editUserProfilePage.getInvalidNumberError()).isDisplayed());
    }

    @Test
    public void existingPhoneNumberErrorTest() {
        logInAndOpenDetails();

        editData(newFirstName, newLastName, existingPhoneNumber);
        driver.findElement(editUserProfilePage.getFirstNameEdit()).sendKeys(Keys.SHIFT);
        assertTrue(driver.findElement(editUserProfilePage.getExistingNumberError()).isDisplayed());
    }

    private void logInAndOpenDetails() {
        adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        AccountsListPage accountsListPage = adminMainPage.openAccountsList();

        driver.navigate().refresh();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts")));

        List<WebElement> tableData = accountsListPage.getTableContent();
        userLogin = tableData.get(0).getText();
        profileDetailsPage = accountsListPage.openAnotherUserProfileDetails(tableData.get(6));

        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(userLogin)));
    }

    private void editData(String firstName, String lastName, String phoneNumber) {
        editUserProfilePage = profileDetailsPage.editUser();
        profileDetailsPage = editUserProfilePage.editUser(firstName, lastName, phoneNumber);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
