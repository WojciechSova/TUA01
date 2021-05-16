package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountsListPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.LoginPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.MainPage;

public class BlockUnblockAccountTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private WebDriver driver;
    private final String url = "https://studapp.it.p.lodz.pl:8402/#";
    private final String adminLogin = "admin";
    private final String adminPassword = "password?";
    private AccountsListPage accountsListPage;
    private String login;

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
        driverWait = new WebDriverWait(driver, 12);
    }

    @Test
    public void blockUnblockAccountTest() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        AdminMainPage adminMainPage = loginPage.loginValidAdmin(adminLogin, adminPassword);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        accountsListPage = adminMainPage.openAccountsList();

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(accountsListPage.getUsersTable()));

        login = accountsListPage.getActiveUserLogin();
        accountsListPage.changeUserActivity(login);
        waitForButtonChanging();

        Assertions.assertFalse(accountsListPage.isUserActive(login));
        accountsListPage.changeUserActivity(login);
        waitForButtonChanging();

        Assertions.assertTrue(accountsListPage.isUserActive(login));
    }

    private void waitForButtonChanging() {
        driverWait.until((ExpectedCondition<Boolean>) driver -> {
            try {
                accountsListPage.getUserWithLogin(login);
            }
            catch (StaleElementReferenceException ex) {
                return true;
            }
            return false;
        });
        driverWait.until(ExpectedConditions
                .presenceOfNestedElementLocatedBy(accountsListPage.getUserWithLogin(login), accountsListPage.getBlockUnblockButton()));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
