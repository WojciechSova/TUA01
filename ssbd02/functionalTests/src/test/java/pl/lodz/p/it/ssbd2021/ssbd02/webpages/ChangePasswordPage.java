package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ChangePasswordPage {

    private final By oldPassword = By.id("old-password");
    private final By newPassword = By.id("new-password");
    private final By newPasswordRepeat = By.id("new-password-repeat");
    private final By confirm = By.id("confirm");
    private final By form = By.id("change-password-form");
    private final By invalidPasswordError = By.id("invalid-password-error");
    private final By samePasswordError = By.id("same-password-error");
    private final By shortPasswordError = By.id("short-password-error");
    private final By differentPasswordsError = By.id("different-passwords-error");
    protected WebDriver driver;

    public ChangePasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    public By getForm() {
        return form;
    }

    public By getInvalidPasswordError() {
        return invalidPasswordError;
    }

    public By getSamePasswordError() {
        return samePasswordError;
    }

    public By getShortPasswordError() {
        return shortPasswordError;
    }

    public By getDifferentPasswordsError() {
        return differentPasswordsError;
    }

    public By getOldPassword() {
        return oldPassword;
    }

    public By getConfirm() {
        return confirm;
    }

    public void changePassword(String oldPassword, String newPassword, String newPasswordRepeat) {
        driver.findElement(this.oldPassword).sendKeys(oldPassword);
        driver.findElement(this.newPassword).sendKeys(newPassword);
        driver.findElement(this.newPasswordRepeat).sendKeys(newPasswordRepeat);
        driver.findElement(this.confirm).click();
    }
}
