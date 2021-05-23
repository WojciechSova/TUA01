package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountDetailsPage {

    protected WebDriver driver;
    private final By changePasswordBtn = By.id("change-password");
    private final By changeEmailBtn = By.id("change-email");

    public AccountDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public ChangePasswordPage openChangePasswordPage() {
        driver.findElement(changePasswordBtn).click();
        return new ChangePasswordPage(driver);
    }

    public ChangeEmailPage openChangeEmailPage() {
        driver.findElement(changeEmailBtn).click();
        return new ChangeEmailPage(driver);
    }
}
