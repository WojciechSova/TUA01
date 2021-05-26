package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage {

    private WebDriver driver;
    private By login = By.id("login-reg");
    private By email = By.id("email-reg");
    private By emailRepeat = By.id("email-reg-repeat");
    private By password = By.id("password-reg");
    private By passwordRepeat = By.id("password-reg-repeat");
    private By firstName = By.id("first-name");
    private By lastName = By.id("last-name");
    private By phoneNumber = By.id("phone-number");
    private By confirmBtn = By.id("confirm");
    private By errorMsg = By.id("invalidCredentialsLabel");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    public void register(String login, String[] emails, String[] passwords, String[] names, String phoneNumber) {
        driver.findElement(this.login).sendKeys(login);
        driver.findElement(this.email).sendKeys(emails[0]);
        driver.findElement(this.emailRepeat).sendKeys(emails[1]);
        driver.findElement(this.password).sendKeys(passwords[0]);
        driver.findElement(this.passwordRepeat).sendKeys(passwords[1]);
        driver.findElement(this.firstName).sendKeys(names[0]);
        driver.findElement(this.lastName).sendKeys(names[1]);
        driver.findElement(this.phoneNumber).sendKeys(phoneNumber);
        driver.findElement(this.confirmBtn).click();
    }

    public By getLogin() {
        return login;
    }

    public By getEmail() {
        return email;
    }

    public By getErrorMsg() {
        return errorMsg;
    }
}
