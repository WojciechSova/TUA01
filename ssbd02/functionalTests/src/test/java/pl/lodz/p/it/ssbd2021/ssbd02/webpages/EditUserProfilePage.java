package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EditUserProfilePage {

    protected WebDriver driver;
    private By firstNameEdit = By.id("edit-firstName");
    private By lastNameEdit = By.id("edit-lastName");
    private By phoneNumberEdit = By.id("edit-phoneNumber");
    private By saveBtn = By.id("saveBtn");
    private By invalidNumberError = By.id("invalid-number-error");
    private By existingNumberError = By.id("existing-number-error");

    public EditUserProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public AccountDetailsPage editUser(String newFirstName, String newLastName, String newPhoneNumber) {
        driver.findElement(firstNameEdit).sendKeys(newFirstName);
        driver.findElement(lastNameEdit).sendKeys(newLastName);
        driver.findElement(phoneNumberEdit).sendKeys(newPhoneNumber);
        driver.findElement(saveBtn).click();

        return new AccountDetailsPage(driver);
    }

    public By getInvalidNumberError() {
        return invalidNumberError;
    }

    public By getExistingNumberError() {
        return existingNumberError;
    }

    public By getFirstNameEdit() {
        return firstNameEdit;
    }

    public By getLastNameEdit() {
        return lastNameEdit;
    }
}
