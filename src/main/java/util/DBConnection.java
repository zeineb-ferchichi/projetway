package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/projetway";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection conn;
    private static DBConnection instance;

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Charger le driver MySQL
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion réussie à la base de données !");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ Driver MySQL non trouvé ! Ajoute mysql-connector-java dans ton projet.", e);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Impossible d'établir la connexion : " + e.getMessage(), e);
        }
    }
}
