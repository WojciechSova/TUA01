package pl.lodz.p.it.ssbd2021.ssbd02;

import java.sql.*;
import java.util.Properties;

public class TestDatabaseConnection {

    private static String url = "jdbc:postgresql://localhost:5432/ssbd02local";

    public static String getOneTimeUrl(String newEmail) {
        Properties props = new Properties();
        props.setProperty("user", "ssbd02admin");
        props.setProperty("password", "admin_password");
        Connection connection;
        Statement statement;
        ResultSet result;
        try {
            connection = DriverManager.getConnection(url, props);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT url FROM public.one_time_url o WHERE o.new_email =\'" + newEmail + "\'");
            result.next();
            return result.getString("url");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
