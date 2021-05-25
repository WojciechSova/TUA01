package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ChangeAccessLevelsPage {

    protected WebDriver driver;
    private final By adminCheckbox = By.id("admin-label");
    private final By employeeCheckbox = By.id("employee-label");
    private final By clientCheckbox = By.id("client-label");
    private final By checkboxContainer = By.className("checkbox-container");
    private final By confirmButton = By.id("confirmButton");

    public ChangeAccessLevelsPage(WebDriver driver) {
        this.driver = driver;
    }

    public By getCheckboxContainer() {
        return checkboxContainer;
    }

    public void changeAccessLevel(String accessLevel) {
        switch (accessLevel) {
            case "ADMIN":
                driver.findElement(adminCheckbox).click();
                break;
            case "EMPLOYEE":
                driver.findElement(employeeCheckbox).click();
                break;
            case "CLIENT":
                driver.findElement(clientCheckbox).click();
                break;
        }
        driver.findElement(confirmButton).click();
    }
}
