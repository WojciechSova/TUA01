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
    private final String url = "https://localhost:8181/#";
    private WebDriver driver;
    private final String adminLogin = "admin";
    private final String adminPassword = "password?";

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
        driver.get(url);
        driverWait = new WebDriverWait(driver, 10);
    }

    @Test
    public void getAllAccountsList() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        AdminMainPage adminMainPage = loginPage.loginValidAdmin(adminLogin, adminPassword);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        Assertions.assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
        Assertions.assertEquals("ADMINISTRATOR", adminMainPage.getLoggedInUserAccessLevel());

        AccountsListPage accountsListPage = adminMainPage.openAccountsList();
        Assertions.assertTrue(accountsListPage.isTableDisplayed());

        List<String> tableHeaders = accountsListPage.getTableHeaders();
        Assertions.assertAll(
                () -> Assertions.assertTrue(tableHeaders.contains("Login")),
                () -> Assertions.assertTrue(tableHeaders.contains("Imię")),
                () -> Assertions.assertTrue(tableHeaders.contains("Nazwisko")),
                () -> Assertions.assertTrue(tableHeaders.contains("Poziomy dostępu")),
                () -> Assertions.assertTrue(tableHeaders.contains("Odblokuj/Zablokuj")),
                () -> Assertions.assertTrue(tableHeaders.contains("Szczegóły"))
        );
    }

    @AfterEach
    void end() {
        driver.quit();
    }
}
