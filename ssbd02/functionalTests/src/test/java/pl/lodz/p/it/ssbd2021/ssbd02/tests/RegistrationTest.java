package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.ClientMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.MainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.RegistrationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegistrationTest {

    private static ChromeOptions options;
    private static WebDriverWait driverWait;
    private final String login = "newLogin";
    private final String email = "newEmail@newEmail.ferrytales";
    private final String password = "Password1234";
    private final String firstName = "Karol";
    private final String lastName = "Karolowy";
    private final String phoneNumber = "000555444";
    private final String existingEmail = "nieistnieje@aaa.pl";
    private final String existingLogin = "admin";
    private final String existingPhoneNumber = "48123456789";
    private WebDriver driver;
    private String oneTimeUrl;
    private String confirmationUrl;

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
    public void registrationTest() {
        MainPage mainPage = new MainPage(driver);
        RegistrationPage registrationPage = mainPage.openRegistrationPage();
        registrationPage.register(login, new String[]{email, email}, new String[]{password, password}, new String[]{firstName, lastName}, phoneNumber);

        driverWait.until(ExpectedConditions.presenceOfElementLocated(mainPage.getLoginButton()));
        oneTimeUrl = TestUtils.getLastOneTimeUrl();
        confirmationUrl = TestUtils.url.concat("/confirm/account/").concat(oneTimeUrl);
        driver.get(confirmationUrl);
        driverWait.until(ExpectedConditions.not(ExpectedConditions.urlMatches(TestUtils.url.concat("/confirm"))));

        ClientMainPage clientMainPage = TestUtils.logInAsNewUser(driver, login, password);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(clientMainPage.getCurrentUser()));

        assertEquals(login.toUpperCase(), driver.findElement(clientMainPage.getCurrentUser()).getText());
        TestUtils.removeLastAccount();
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "login", "phoneNumber"})
    public void registrationFailureTest(String conflictedValue) {
        MainPage mainPage = new MainPage(driver);
        RegistrationPage registrationPage = mainPage.openRegistrationPage();
        switch (conflictedValue) {
            case "email": {
                registrationPage.register(login, new String[]{existingEmail, existingEmail}, new String[]{password, password}, new String[]{firstName, lastName}, phoneNumber);
                driver.findElement(registrationPage.getLogin()).sendKeys(Keys.SHIFT);
                driverWait.until(ExpectedConditions.presenceOfElementLocated(registrationPage.getErrorMsg()));
                assertTrue(driver.findElement(registrationPage.getErrorMsg()).isDisplayed());
                break;
            }
            case "login": {
                registrationPage.register(existingLogin, new String[]{email, email}, new String[]{password, password}, new String[]{firstName, lastName}, phoneNumber);
                driver.findElement(registrationPage.getLogin()).sendKeys(Keys.SHIFT);
                driverWait.until(ExpectedConditions.presenceOfElementLocated(registrationPage.getErrorMsg()));
                assertTrue(driver.findElement(registrationPage.getErrorMsg()).isDisplayed());
                break;
            }
            case "phoneNumber": {
                registrationPage.register(login, new String[]{email, email}, new String[]{password, password}, new String[]{firstName, lastName}, existingPhoneNumber);
                driver.findElement(registrationPage.getLogin()).sendKeys(Keys.SHIFT);
                driverWait.until(ExpectedConditions.presenceOfElementLocated(registrationPage.getErrorMsg()));
                assertTrue(driver.findElement(registrationPage.getErrorMsg()).isDisplayed());
                break;
            }
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
