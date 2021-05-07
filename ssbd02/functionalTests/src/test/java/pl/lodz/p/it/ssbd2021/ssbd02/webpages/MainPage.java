package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {

    protected WebDriver driver;
    private final By loginButton = By.id("loginLink");
    //TODO: Add other main page web elements required for further testing

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage openLoginForm() {
        driver.findElement(loginButton).click();
        return new LoginPage(driver);
    }
}
