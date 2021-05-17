package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.openqa.selenium.WebDriver;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.*;

public class TestUtils {

    public static final String adminLogin = "admin";
    public static final String adminPassword = "password?";
    public static final String employeeLogin = "pracownik";
    public static final String employeePassword = "password?";
    public static final String clientLogin = "klient1";
    public static final String clientPassword = "password?";
    public static final String url = "https://studapp.it.p.lodz.pl:8402/#";

    public static AdminMainPage logInAsAdmin(WebDriver driver) {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        return loginPage.loginValidAdmin(adminLogin, adminPassword);
    }

    public static EmployeeMainPage logInAsEmployee(WebDriver driver) {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        return loginPage.loginValidEmployee(employeeLogin, employeePassword);
    }

    public static ClientMainPage logInAsClient(WebDriver driver) {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        return loginPage.loginValidClient(clientLogin, clientPassword);
    }
}
