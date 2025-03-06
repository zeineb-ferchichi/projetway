package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogService {
    public static void logAction(String action) {
        String query = "INSERT INTO logs (action) VALUES (?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, action);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
