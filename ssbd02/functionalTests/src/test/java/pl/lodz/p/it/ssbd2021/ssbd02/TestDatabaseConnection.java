package pl.lodz.p.it.ssbd2021.ssbd02;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;

public class TestDatabaseConnection {

    private static final Logger logger = LogManager.getLogger();
    private static String url = "jdbc:postgresql://localhost:5432/ssbd02local";

    public static String getOneTimeUrl(String newEmail) {
        Properties properties = new Properties();
        properties.setProperty("user", "ssbd02admin");
        properties.setProperty("password", "admin_password");
        Connection connection;
        Statement statement;
        ResultSet result;
        try {
            connection = DriverManager.getConnection(url, properties);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT url FROM public.one_time_url o WHERE o.new_email =\'" + newEmail + "\'");
            result.next();
            return result.getString("url");
        } catch (SQLException ex) {
            logger.log(Level.WARN, ex);
        }
        return "";
    }
}
