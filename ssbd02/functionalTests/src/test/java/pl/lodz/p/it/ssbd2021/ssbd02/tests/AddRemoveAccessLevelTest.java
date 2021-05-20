package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AccountsListPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.ChangeAccessLevelsPage;

public class AddRemoveAccessLevelTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private WebDriver driver;
    private final String login = "admin";

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
        driverWait = new WebDriverWait(driver, 12);
    }

    @Test
    public void addRemoveAccessLevelTest() {
        AdminMainPage adminMainPage = TestUtils.logInAsAdmin(driver);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        AccountsListPage accountsListPage = adminMainPage.openAccountsList();

        driverWait.until(ExpectedConditions.visibilityOfElementLocated(accountsListPage.getUsersTable()));

        Assertions.assertEquals("ADMIN", accountsListPage.getAccessLevels(login));

        ChangeAccessLevelsPage changeAccessLevelsPage = accountsListPage.openChangeAccessLevelsForm(login);
        changeAccessLevelsPage.changeAccessLevel("EMPLOYEE");

        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(changeAccessLevelsPage.getCheckboxContainer()));

        Assertions.assertAll(
                () -> Assertions.assertTrue(accountsListPage.getAccessLevels(login).contains("ADMIN")),
                () -> Assertions.assertTrue(accountsListPage.getAccessLevels(login).contains("EMPLOYEE"))
        );

        accountsListPage.openChangeAccessLevelsForm(login);
        changeAccessLevelsPage.changeAccessLevel("EMPLOYEE");

        driverWait.until(ExpectedConditions.invisibilityOfElementLocated(changeAccessLevelsPage.getCheckboxContainer()));

        Assertions.assertAll(
                () -> Assertions.assertTrue(accountsListPage.getAccessLevels(login).contains("ADMIN")),
                () -> Assertions.assertFalse(accountsListPage.getAccessLevels(login).contains("EMPLOYEE"))
        );
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
