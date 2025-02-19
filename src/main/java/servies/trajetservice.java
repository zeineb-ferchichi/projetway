package servies;

import entities.trajet;
import util.Datasource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class trajetservice implements service<trajet> {

    private final Connection conn;

    public trajetservice() {
        this.conn = Datasource.getInstance().getConnection();
    }

    @Override
    public void add(trajet trajet) {
        String sql = "INSERT INTO trajet (idvoyage, type_transport, compagnie, numero, date_depart, date_arrivee, ville_depart, ville_arrivee, cout) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, trajet.getIdvoyage());
            pstmt.setString(2, trajet.getType_transport());
            pstmt.setString(3, trajet.getCompagnie());
            pstmt.setString(4, trajet.getNumero());
            pstmt.setString(5, trajet.getDate_depart());
            pstmt.setString(6, trajet.getDate_arrivee());
            pstmt.setString(7, trajet.getVille_depart());
            pstmt.setString(8, trajet.getVille_arrivee());
            pstmt.setDouble(9, trajet.getCout());
            pstmt.executeUpdate();

            // Retrieve the auto-generated trajet_id
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trajet.setTrajet_id(generatedKeys.getInt(1)); // Set the generated ID to the trajet object
                } else {
                    throw new SQLException("Failed to retrieve auto-generated ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding trajet: " + e.getMessage());
        }
    }

    @Override
    public void update(trajet trajet) {
        String sql = "UPDATE trajet SET idvoyage = ?, type_transport = ?, compagnie = ?, numero = ?, date_depart = ?, date_arrivee = ?, ville_depart = ?, ville_arrivee = ?, cout = ? WHERE trajet_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trajet.getIdvoyage());
            pstmt.setString(2, trajet.getType_transport());
            pstmt.setString(3, trajet.getCompagnie());
            pstmt.setString(4, trajet.getNumero());
            pstmt.setString(5, trajet.getDate_depart());
            pstmt.setString(6, trajet.getDate_arrivee());
            pstmt.setString(7, trajet.getVille_depart());
            pstmt.setString(8, trajet.getVille_arrivee());
            pstmt.setDouble(9, trajet.getCout());
            pstmt.setInt(10, trajet.getTrajet_id());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating trajet: " + e.getMessage());
        }
    }

    @Override
    public void delete(int trajet_id) {
        String sql = "DELETE FROM trajet WHERE trajet_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trajet_id); // Use ID directly
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting trajet: " + e.getMessage());
        }
    }

    @Override
    public List<trajet> getAll() {
        List<trajet> trajets = new ArrayList<>();
        String sql = "SELECT * FROM trajet";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                trajet trajet = new trajet();
                trajet.setTrajet_id(rs.getInt("trajet_id")); // Set the trajet_id
                trajet.setIdvoyage(rs.getInt("idvoyage"));
                trajet.setType_transport(rs.getString("type_transport"));
                trajet.setCompagnie(rs.getString("compagnie"));
                trajet.setNumero(rs.getString("numero"));
                trajet.setDate_depart(rs.getString("date_depart"));
                trajet.setDate_arrivee(rs.getString("date_arrivee"));
                trajet.setVille_depart(rs.getString("ville_depart"));
                trajet.setVille_arrivee(rs.getString("ville_arrivee"));
                trajet.setCout(rs.getDouble("cout"));
                trajets.add(trajet);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving trajets: " + e.getMessage());
        }
        return trajets;
    }
}
