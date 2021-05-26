package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    protected WebDriver driver;
    private final By loginField = By.id("login");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("loginFormBtn");
    private final By forgotPasswordLink = By.id("forgot-password");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public AdminMainPage loginValidAdmin(String login, String password) {
        driver.findElement(loginField).sendKeys(login);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        return new AdminMainPage(driver);
    }

    public EmployeeMainPage loginValidEmployee(String login, String password) {
        driver.findElement(loginField).sendKeys(login);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        return new EmployeeMainPage(driver);
    }

    public ClientMainPage loginValidClient(String login, String password) {
        driver.findElement(loginField).sendKeys(login);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        return new ClientMainPage(driver);
    }

    public PasswordResetPage openPasswordResetPage() {
        driver.findElement(forgotPasswordLink).click();
        return new PasswordResetPage(driver);
    }
}
