package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditAnotherUserProfileTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private WebDriver driver;
    private final String url = "https://localhost:8181/#";
    private final String adminLogin = "admin";
    private final String adminPassword = "password?";
    private final String newFirstName = "noweImie";
    private final String newLastName = "noweNazwisko";
    private final String newPhoneNumber = "48000000000";
    private ProfileDetailsPage profileDetailsPage;
    private EditUserProfilePage editUserProfilePage;

    @BeforeAll
    static void initAll() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        //options.setHeadless(true);
    }

    @BeforeEach
    public void initEach() {
        driver = new ChromeDriver(options);
        driver.get(url);
        driverWait = new WebDriverWait(driver, 10);
    }

    @Test
    public void editUserTest() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        AdminMainPage adminMainPage = loginPage.loginValidAdmin(adminLogin, adminPassword);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        AccountsListPage accountsListPage = adminMainPage.openAccountsList();

        driver.navigate().refresh();
        driverWait.until(ExpectedConditions.urlMatches(url.concat("/ferrytales/accounts")));

        List<WebElement> tableData = accountsListPage.getTableContent();
        String userLogin = tableData.get(0).getText();
        profileDetailsPage = accountsListPage.openAnotherUserProfileDetails(tableData.get(6));

        driverWait.until(ExpectedConditions.urlMatches(url.concat("/ferrytales/accounts/").concat(userLogin)));

        String firstName = driver.findElement(profileDetailsPage.getFirstNameField()).getText();
        String lastName = driver.findElement(profileDetailsPage.getLastNameField()).getText();
        String phoneNumber = driver.findElement(profileDetailsPage.getPhoneNumberField()).getText();

        editData(newFirstName, newLastName, newPhoneNumber);

        driverWait.until(ExpectedConditions.urlMatches(url.concat("/ferrytales/accounts/").concat(userLogin)));

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

    private void editData(String firstName, String lastName, String phoneNumber) {
        editUserProfilePage = profileDetailsPage.editUser();
        profileDetailsPage = editUserProfilePage.editUser(firstName, lastName, phoneNumber);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
