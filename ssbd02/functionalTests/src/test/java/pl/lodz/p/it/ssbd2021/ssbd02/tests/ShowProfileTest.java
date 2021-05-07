package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.LoginPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.MainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.OwnProfileDetailsPage;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ShowProfileTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private static WebDriver driver;
    private final String url = "https://localhost:8181/#";
    private final String adminLogin = "admin";
    private final String adminPassword = "password?";
    private final String adminFirstName = "Kazimierz";
    private final String adminLastName = "Andrzejewski";
    private final String adminEmail = "nieistnieje@aaa.pl";

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
    public void showOwnProfileAdmin() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        AdminMainPage adminMainPage = loginPage.loginValidAdmin(adminLogin, adminPassword);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(adminMainPage.getCurrentUser()));

        assertEquals("ADMIN", adminMainPage.getLoggedInUserLogin());
        assertEquals("ADMINISTRATOR", adminMainPage.getLoggedInUserAccessLevel());

        OwnProfileDetailsPage ownProfileDetailsPage = adminMainPage.openOwnProfileDetails();
        driverWait.until(ExpectedConditions.urlMatches(url.concat("/ferrytales/account")));
        assertTrue(ownProfileDetailsPage.areProperFieldsDisplayed("ADMIN"));
        List<String> adminData = ownProfileDetailsPage.getData();
        assertAll(
                () -> assertEquals(adminData.get(0), adminLogin),
                () -> assertEquals(adminData.get(1), adminFirstName),
                () -> assertEquals(adminData.get(2), adminLastName),
                () -> assertEquals(adminData.get(3), adminEmail),
                () -> assertEquals(adminData.get(6), Boolean.TRUE.toString().toLowerCase(Locale.ROOT)),
                () -> assertEquals(adminData.get(7), Boolean.TRUE.toString().toLowerCase(Locale.ROOT))
        );
        assertThrows(NoSuchElementException.class, () -> ownProfileDetailsPage.areProperFieldsDisplayed("CLIENT"));
    }

    @AfterEach
    void tearDown() {
        driver.close();
    }
}
