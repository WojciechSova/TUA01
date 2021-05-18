package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountsListPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.ProfileDetailsPage;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ShowProfileTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private static WebDriver driver;
    private final String adminLogin = "admin";
    private final String adminFirstName = "Kazimierz";
    private final String adminLastName = "Andrzejewski";
    private final String adminEmail = "nieistnieje@aaa.pl";

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
    public void showOwnProfileAdmin() {
        AdminMainPage adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
        assertEquals("ADMINISTRATOR", adminMainPage.getLoggedInUserAccessLevel());

        ProfileDetailsPage profileDetailsPage = adminMainPage.openOwnProfileDetails();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/account")));
        assertTrue(profileDetailsPage.areProperFieldsDisplayed("ADMIN"));
        List<String> adminData = profileDetailsPage.getData();
        assertAll(
                () -> assertEquals(adminData.get(0), adminLogin),
                () -> assertEquals(adminData.get(1), adminFirstName),
                () -> assertEquals(adminData.get(2), adminLastName),
                () -> assertEquals(adminData.get(3), adminEmail),
                () -> assertEquals(adminData.get(7), Boolean.TRUE.toString().toLowerCase(Locale.ROOT)),
                () -> assertEquals(adminData.get(8), Boolean.TRUE.toString().toLowerCase(Locale.ROOT))
        );
        assertDoesNotThrow(() -> profileDetailsPage.areProperFieldsDisplayed("ADMIN"));
    }

    @Test
    public void showAnotherUserProfile() {
        AdminMainPage adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
        assertEquals("ADMINISTRATOR", adminMainPage.getLoggedInUserAccessLevel());

        AccountsListPage accountsListPage = adminMainPage.openAccountsList();
        driverWait.until(ExpectedConditions.presenceOfElementLocated(accountsListPage.getUsersTable()));

        List<String> tableData = accountsListPage.getTableData();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts")));
        driverWait.until(ExpectedConditions.presenceOfElementLocated(accountsListPage.getUsersTable()));

        ProfileDetailsPage profileDetailsPage = accountsListPage.openAnotherUserProfileDetails();
        driverWait.until(ExpectedConditions.urlMatches(TestUtils.url.concat("/ferrytales/accounts/").concat(tableData.get(0))));

        assertTrue(profileDetailsPage.areProperFieldsDisplayed(Arrays.stream(tableData.get(3).split("\n")).findFirst().get()));
    }

    @AfterEach
    void tearDown() {
        driver.close();
    }
}
