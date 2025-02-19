package tn.esprit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private String url = "jdbc:mysql://localhost:3306/projetway";
    private String user = "root";
    private String password = "";
    private static DBConnection instance;
    private Connection conn;

    // Singleton pattern
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // Get the database connection
    public Connection getConn() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connection established");
            } catch (SQLException e) {
                System.out.println("Error establishing connection: " + e.getMessage());
            }
        }
        return conn;
    }

    // Constructor is private to prevent direct instantiation
    private DBConnection() {
        // Connection initialization is deferred until it's needed
    }
}
