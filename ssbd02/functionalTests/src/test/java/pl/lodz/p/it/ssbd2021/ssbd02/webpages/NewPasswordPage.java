package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class NewPasswordPage {

    protected WebDriver driver;
    private final By newPasswordResetForm = By.id("new-password-reset-form");
    private final By newPasswordField = By.id("new-password");
    private final By repeatNewPasswordField = By.id("new-password-repeat");
    private final By confirmPasswordResetBtn = By.id("confirm");
    private final By shortPasswordError = By.id("short-password-error");
    private final By differentPasswordsError = By.id("different-passwords-error");

    public NewPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setNewPassword(String newPassword, String newPasswordRepeat) {
        driver.findElement(newPasswordField).sendKeys(newPassword);
        driver.findElement(repeatNewPasswordField).sendKeys(newPasswordRepeat);
        driver.findElement(newPasswordField).sendKeys(Keys.TAB);
        driver.findElement(confirmPasswordResetBtn).click();
    }

    public By getNewPasswordResetForm() {
        return newPasswordResetForm;
    }

    public By getShortPasswordError() {
        return shortPasswordError;
    }

    public By getDifferentPasswordsError() {
        return differentPasswordsError;
    }


}
