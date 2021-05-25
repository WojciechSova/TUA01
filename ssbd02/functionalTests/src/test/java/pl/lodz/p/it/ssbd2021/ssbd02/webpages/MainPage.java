package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {

    protected WebDriver driver;
    private final By aboutUsLink = By.id("aboutUsLink");
    private final By contactLink = By.id("contactLink");
    private final By currentCruisesLink = By.id("currentCruisesLink");
    private final By loginLink = By.id("loginLink");
    private final By registerLink = By.id("registerLink");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage openLoginForm() {
        driver.findElement(loginLink).click();
        return new LoginPage(driver);
    }

    public boolean areMainPageNavBarLinksDisplayed() {
        return driver.findElement(aboutUsLink).isDisplayed() &&
                driver.findElement(contactLink).isDisplayed() &&
                driver.findElement(currentCruisesLink).isDisplayed() &&
                driver.findElement(loginLink).isDisplayed() &&
                driver.findElement(registerLink).isDisplayed();
    }
}
