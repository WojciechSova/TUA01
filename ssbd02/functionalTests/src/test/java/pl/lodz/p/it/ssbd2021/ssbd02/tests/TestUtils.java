package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.openqa.selenium.WebDriver;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.*;

import java.sql.*;
import java.util.Properties;

public class TestUtils {

    public static final String adminLogin = "admin";
    public static final String adminPassword = "password?";
    public static final String employeeLogin = "pracownik";
    public static final String employeePassword = "password?";
    public static final String clientLogin = "klient1";
    public static final String clientPassword = "password?";
    public static final String url = "https://studapp.it.p.lodz.pl:8402/#";
    public static final String dbUrl = "jdbc:postgresql://localhost:5432/ssbd02local";

    public static AdminMainPage logInAsAdmin(WebDriver driver) {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        return loginPage.loginValidAdmin(adminLogin, adminPassword);
    }

    public static AdminMainPage logInAsAdmin(WebDriver driver, String login, String password) {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        return loginPage.loginValidAdmin(login, password);
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

    public static String getOneTimeUrl(String newEmail) {
        Properties properties = new Properties();
        properties.setProperty("user", "ssbd02admin");
        properties.setProperty("password", "admin_password");
        Connection connection;
        Statement statement;
        ResultSet result;
        try {
            connection = DriverManager.getConnection(dbUrl, properties);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT url FROM public.one_time_url o WHERE o.new_email ='" + newEmail + "'");
            result.next();
            return result.getString("url");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
