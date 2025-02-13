package servies;

import entities.voyage;
import util.Datasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class voyageservice implements service<voyage> {
    private Connection conn;

    public voyageservice() {
        this.conn = Datasource.getInstance().getConnection();
    }

    @Override
    public void add(voyage voyage) {
        String sql = "INSERT INTO voyage (destination, date_depart, date_retour) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, voyage.getDestination());
            pstmt.setString(2, voyage.getDate_depart());
            pstmt.setString(3, voyage.getDate_retour());
            pstmt.executeUpdate();

            // Retrieve the auto-generated idvoyage
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    voyage.setIdvoyage(generatedKeys.getInt(1)); // Set the generated ID to the Voyage object
                } else {
                    throw new SQLException("Failed to retrieve auto-generated ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding voyage: " + e.getMessage());
        }
    }

    @Override
    public void update(voyage voyage) {
        String sql = "UPDATE voyage SET destination = ?, date_depart = ?, date_retour = ? WHERE idvoyage = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voyage.getDestination());
            pstmt.setString(2, voyage.getDate_depart());
            pstmt.setString(3, voyage.getDate_retour());
            pstmt.setInt(4, voyage.getIdvoyage());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating voyage: " + e.getMessage());
        }
    }

    @Override
    public void delete(voyage voyage) {
        String sql = "DELETE FROM voyage WHERE idvoyage = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, voyage.getIdvoyage());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting voyage: " + e.getMessage());
        }
    }

    @Override
    public List<voyage> getAll() {
        List<voyage> voyages = new ArrayList<>();
        String sql = "SELECT * FROM voyage";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                voyage voyage = new voyage();
                voyage.setIdvoyage(rs.getInt("idvoyage"));
                voyage.setDestination(rs.getString("destination"));
                voyage.setDate_depart(rs.getString("date_depart"));
                voyage.setDate_retour(rs.getString("date_retour"));
                voyages.add(voyage);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving voyages: " + e.getMessage());
        }
        return voyages;
    }

    // Optionally, close the connection when the service is no longer needed
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}