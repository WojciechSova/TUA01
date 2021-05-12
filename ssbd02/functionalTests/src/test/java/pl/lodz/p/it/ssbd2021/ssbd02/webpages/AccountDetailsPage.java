package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountDetailsPage {

    protected WebDriver driver;
    private final By changePasswordBtn = By.id("change-password");

    public AccountDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public ChangePasswordPage openChangePasswordPage() {
        driver.switchTo().defaultContent();
        driver.findElement(changePasswordBtn).click();
        return new ChangePasswordPage(driver);
    }
}
