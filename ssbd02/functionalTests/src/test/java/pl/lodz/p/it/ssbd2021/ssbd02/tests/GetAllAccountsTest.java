package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountsListPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.LoginPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.MainPage;

import java.util.List;

public class GetAllAccountsTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
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
    public void getAllAccountsList() {
        AdminMainPage adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        Assertions.assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
        Assertions.assertEquals("ADMINISTRATOR", adminMainPage.getLoggedInUserAccessLevel());

        AccountsListPage accountsListPage = adminMainPage.openAccountsList();
        Assertions.assertTrue(accountsListPage.isTableDisplayed());

        List<String> tableHeaders = accountsListPage.getTableHeaders();
        Assertions.assertAll(
                () -> Assertions.assertTrue(tableHeaders.contains("login")),
                () -> Assertions.assertTrue(tableHeaders.contains("imię")),
                () -> Assertions.assertTrue(tableHeaders.contains("nazwisko")),
                () -> Assertions.assertTrue(tableHeaders.contains("poziomy dostępu")),
                () -> Assertions.assertTrue(tableHeaders.contains("odblokuj/zablokuj")),
                () -> Assertions.assertTrue(tableHeaders.contains("zarządzaj")),
                () -> Assertions.assertTrue(tableHeaders.contains("szczegóły"))
        );
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
