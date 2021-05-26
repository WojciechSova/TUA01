package pl.lodz.p.it.ssbd2021.ssbd02.tests;

import org.openqa.selenium.WebDriver;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.AdminMainPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.LoginPage;
import pl.lodz.p.it.ssbd2021.ssbd02.webpages.MainPage;

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

    public static String getOneTimeUrl(String searchBy, String query) {
        Properties properties = new Properties();
        properties.setProperty("user", "ssbd02admin");
        properties.setProperty("password", "admin_password");
        Connection connection;
        Statement statement;
        ResultSet result;
        try {
            connection = DriverManager.getConnection(dbUrl, properties);
            statement = connection.createStatement();
            result = statement.executeQuery(query.concat(searchBy).concat("'"));
            result.next();
            return result.getString("url");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
