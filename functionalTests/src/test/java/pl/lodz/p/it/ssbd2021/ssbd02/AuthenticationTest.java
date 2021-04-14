package pl.lodz.p.it.ssbd2021.ssbd02;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuthenticationTest {

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
        driver.get("https://localhost:8181/");

        driverWait = new WebDriverWait(driver, 10);
    }

    @Test
    public void authenticationCompletedTest() {
        driver.findElement(By.xpath("/html/body/app-root/div/div[2]/div[2]/a[4]"))
                .click();

        Assertions.assertEquals(driver.getCurrentUrl(), "https://localhost:8181/login");

        driver.findElement(By.id("login"))
                .sendKeys("admin");
        driver.findElement(By.id("password"))
                .sendKeys("password?");
        driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/form/button"))
                .click();

        driverWait.until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("login"))));

        Assertions.assertEquals("https://localhost:8181/", driver.getCurrentUrl());

        String role = driver.findElement(By.xpath("/html/body/app-root/div/div[2]/div[2]/div/a[1]")).getText();
        String login = driver.findElement(By.xpath("/html/body/app-root/div/div[2]/div[2]/div/a[2]")).getText();

        Assertions.assertEquals("administrator", role.toLowerCase());
        Assertions.assertEquals("admin", login.toLowerCase());
    }

    @Test
    public void authenticationUncompletedTest() {
        driver.findElement(By.xpath("/html/body/app-root/div/div[2]/div[2]/a[4]"))
                .click();

        Assertions.assertEquals("https://localhost:8181/login", driver.getCurrentUrl());

        driver.findElement(By.id("login"))
                .sendKeys("nieprawidlowe");
        driver.findElement(By.id("password"))
                .sendKeys("password?");
        driver.findElement(By.xpath("/html/body/app-root/app-login/div/div/form/button"))
                .click();

        Assertions.assertEquals("https://localhost:8181/login", driver.getCurrentUrl());
    }

    @AfterEach
    void end() {
        driver.close();
    }
}
