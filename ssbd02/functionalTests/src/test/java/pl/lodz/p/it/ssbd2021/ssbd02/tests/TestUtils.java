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

    public static ClientMainPage logInAsNewUser(WebDriver driver, String login, String password) {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.openLoginForm();
        return loginPage.loginValidClient(login, password);
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

    public static void removeLastAccount() {
        Properties properties = new Properties();
        properties.setProperty("user", "ssbd02admin");
        properties.setProperty("password", "admin_password");
        Connection connection;
        Statement statement;
        try {
            connection = DriverManager.getConnection(dbUrl, properties);
            statement = connection.createStatement();
            String q1 = " DELETE FROM public.one_time_url WHERE id in " +
                    "(SELECT id FROM public.one_time_url ORDER BY id DESC LIMIT 1)";
            String q2 = " DELETE FROM public.access_level WHERE id in " +
                    "(SELECT id FROM public.access_level ORDER BY id DESC LIMIT 1)";
            String q3 = " DELETE FROM public.personal_data WHERE id in " +
                    "(SELECT id FROM public.personal_data ORDER BY id DESC LIMIT 1)";
            String q4 = " DELETE FROM public.account WHERE id in " +
                    "(SELECT id FROM public.account ORDER BY id DESC LIMIT 1)";
            statement.addBatch(q1);
            statement.addBatch(q2);
            statement.addBatch(q3);
            statement.addBatch(q4);
            statement.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static String getLastOneTimeUrl() {
        Properties properties = new Properties();
        properties.setProperty("user", "ssbd02admin");
        properties.setProperty("password", "admin_password");
        Connection connection;
        Statement statement;
        ResultSet result;
        try {
            connection = DriverManager.getConnection(dbUrl, properties);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT url FROM public.one_time_url");
            result.next();
            return result.getString("url");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
