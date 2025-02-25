package util; // ou le package que tu utilises

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection conn;

    // Assure-toi que ces infos sont correctes
    private final String url = "jdbc:mysql://localhost:3308/projetway?autoReconnect=true&useSSL=false";
    private final String user = "root";
    private final String password = "";

    private DBConnection() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion établie avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }
}