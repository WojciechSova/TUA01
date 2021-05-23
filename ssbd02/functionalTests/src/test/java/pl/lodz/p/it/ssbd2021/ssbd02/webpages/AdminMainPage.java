package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminMainPage {

    protected WebDriver driver;
    private final By currentUser = By.id("usernameMain");
    private final By currentUsersLevel = By.id("currentLevel");
    private final By accountsLink = By.id("accountsLink");
    private final By errorMessageId = By.id("invalidCredentialsLabel");
    private final By logOutLink = By.id("logoutLink");

    public AdminMainPage(WebDriver driver) {
        this.driver = driver;
    }

    public By getCurrentUser() {
        return currentUser;
    }

    public String getLoggedInUserLogin() {
        return driver.findElement(currentUser).getText();
    }

    public String getLoggedInUserAccessLevel() {
        return driver.findElement(currentUsersLevel).getText();
    }

    public AccountsListPage openAccountsList() {
        driver.switchTo().defaultContent();
        driver.findElement(accountsLink).click();
        return new AccountsListPage(driver);
    }

    public AccountDetailsPage openOwnAccountDetails() {
        driver.switchTo().defaultContent();
        driver.findElement(currentUser).click();
        return new AccountDetailsPage(driver);
    }

    public boolean isLoginErrorMessageDisplayed() {
        driver.switchTo().defaultContent();
        return driver.findElement(errorMessageId).isDisplayed();
    }

    public By getErrorMessageId() {
        return errorMessageId;
    }

    public MainPage logOut() {
        driver.switchTo().defaultContent();
        driver.findElement(logOutLink).click();
        return new MainPage(driver);
    }
}
