package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class PasswordResetPage {

    protected WebDriver driver;
    private final By emailField = By.id("email");
    private final By resetPasswordBtn = By.id("resetPasswordFormBtn");
    private final By invalidEmailFormatError = By.id("invalid-email-format-error");

    public PasswordResetPage(WebDriver driver) {
        this.driver = driver;
    }

    public void provideEmailAddress(String email) {
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(emailField).sendKeys(Keys.TAB);
        driver.findElement(resetPasswordBtn).click();
    }

    public By getInvalidEmailFormatError() {
        return invalidEmailFormatError;
    }
}
