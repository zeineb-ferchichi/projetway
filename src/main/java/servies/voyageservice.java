package servies;

import entities.voyage;
import util.Datasource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class voyageservice implements service<voyage> {
    private final Connection conn;

    public voyageservice() {
        this.conn = Datasource.getInstance().getConnection();
    }

    @Override
    public void add(voyage voyage) {
        // Validate that the departure date is before the return date
        if (!voyage.getDate_depart().isBefore(voyage.getDate_retour())) {
            throw new IllegalArgumentException("La date de départ doit être avant la date de retour.");
        }

        String sql = "INSERT INTO voyage (destination, date_depart, date_retour) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Use the enum's name() for destination
            pstmt.setString(1, voyage.getDestination().name());
            // Convert LocalDate to java.sql.Date
            pstmt.setDate(2, Date.valueOf(voyage.getDate_depart()));
            pstmt.setDate(3, Date.valueOf(voyage.getDate_retour()));
            pstmt.executeUpdate();

            // Retrieve the auto-generated idvoyage
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    voyage.setIdvoyage(generatedKeys.getInt(1));
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
        // Validate that the departure date is before the return date
        if (!voyage.getDate_depart().isBefore(voyage.getDate_retour())) {
            throw new IllegalArgumentException("La date de départ doit être avant la date de retour.");
        }

        String sql = "UPDATE voyage SET destination = ?, date_depart = ?, date_retour = ? WHERE id_voyage = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voyage.getDestination().name());
            pstmt.setDate(2, Date.valueOf(voyage.getDate_depart()));
            pstmt.setDate(3, Date.valueOf(voyage.getDate_retour()));
            pstmt.setInt(4, voyage.getIdvoyage());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating voyage: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM voyage WHERE id_voyage = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting voyage: " + e.getMessage());
        }
    }

    public List<voyage> getAll() {
        List<voyage> voyages = new ArrayList<>();
        String query = "SELECT id_voyage, destination, date_depart, date_retour FROM voyage"; // Adjust table name if needed

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id_voyage");
                String destinationStr = rs.getString("destination");
                LocalDate dateDepart = rs.getDate("date_depart") != null ? rs.getDate("date_depart").toLocalDate() : null;
                LocalDate dateRetour = rs.getDate("date_retour") != null ? rs.getDate("date_retour").toLocalDate() : null;

                // Handle potential null values before creating voyage object
                if (dateDepart == null || dateRetour == null) {
                    System.out.println("Error: Voyage with ID " + id + " has null dates!");
                    continue; // Skip this entry to prevent exceptions
                }

                voyage.Destination destination = voyage.Destination.valueOf(destinationStr);
                voyage v = new voyage(id, destination, dateDepart, dateRetour);

                voyages.add(v);
                System.out.println("Fetched: " + v);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return voyages;
    }
}