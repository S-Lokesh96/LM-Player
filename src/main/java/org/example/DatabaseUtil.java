package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/music_player";
        String user = "root";
        String password = "Lokesh@96";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the JDBC driver
            return DriverManager.getConnection(url, user, password); // Establish the connection
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("exception");
            return null; // Return null if connection fails
        }
    }
}

