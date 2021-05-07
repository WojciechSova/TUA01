package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminMainPage {

    protected WebDriver driver;
    private final By currentUser = By.id("usernameMain");
    private final By currentUsersLevel = By.id("currentLevel");
    private final By accountsButton = By.xpath("/html/body/app-root/app-main-page/div/div/app-navigation/div[1]/div[2]/app-links/div/a[1]");

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
        driver.findElement(accountsButton).click();
        return new AccountsListPage(driver);
    }
}
