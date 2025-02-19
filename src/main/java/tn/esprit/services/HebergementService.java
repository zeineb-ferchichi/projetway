package tn.esprit.services;

import tn.esprit.models.Hebergement;
import tn.esprit.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementService implements IService<Hebergement> {
    private final Connection conn;

    public HebergementService() {
        this.conn = DBConnection.getInstance().getConn();
        if (this.conn == null) {
            System.out.println("Error: Database connection failed to initialize in HebergementService.");
        } else {
            System.out.println("Database connection initialized successfully.");
        }
    }



    @Override
    public void add(Hebergement hebergement) {
        String SQL = "INSERT INTO Hebergement (nom, type, adresse, ville, pays, capacite, prix) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Ensure the connection is not null
        if (conn == null) {
            System.out.println("Error: Connection is null.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, hebergement.getNom());
            pstmt.setString(2, hebergement.getType());
            pstmt.setString(3, hebergement.getAdresse());
            pstmt.setString(4, hebergement.getVille());
            pstmt.setString(5, hebergement.getPays());
            pstmt.setInt(6, hebergement.getCapacite());
            pstmt.setDouble(7, hebergement.getPrix());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Hebergement added successfully!");
            } else {
                System.out.println("Failed to add Hebergement.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding Hebergement: " + e.getMessage());
            e.printStackTrace();  // Print the full exception stack trace for better debugging
        }
    }

    @Override
    public void update(Hebergement hebergement) {
        String SQL = "UPDATE Hebergement SET nom = ?, type = ?, adresse = ?, ville = ?, pays = ?, capacite = ?, prix = ? WHERE id = ?";

        // Ensure the connection is valid
        if (conn == null) {
            System.out.println("Error: Connection is null.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, hebergement.getNom());
            pstmt.setString(2, hebergement.getType());
            pstmt.setString(3, hebergement.getAdresse());
            pstmt.setString(4, hebergement.getVille());
            pstmt.setString(5, hebergement.getPays());
            pstmt.setInt(6, hebergement.getCapacite());
            pstmt.setInt(7, hebergement.getPrix());
            pstmt.setInt(8, hebergement.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Hebergement updated successfully!");
            } else {
                System.out.println("No record found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating Hebergement: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String SQL = "DELETE FROM Hebergement WHERE id = ?";

        // Ensure the connection is valid
        if (conn == null) {
            System.out.println("Error: Connection is null.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Hebergement deleted successfully!");
            } else {
                System.out.println("No record found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting Hebergement: " + e.getMessage());
        }
    }

    @Override
    public List<Hebergement> getAll() {
        String SQL = "SELECT * FROM Hebergement";
        List<Hebergement> hebergementList = new ArrayList<>();

        // Ensure the connection is valid
        if (conn == null) {
            System.out.println("Error: Connection is null.");
            return hebergementList;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                Hebergement h = new Hebergement();
                h.setId(rs.getInt("id"));
                h.setNom(rs.getString("nom"));
                h.setType(rs.getString("type"));
                h.setAdresse(rs.getString("adresse"));
                h.setVille(rs.getString("ville"));
                h.setPays(rs.getString("pays"));
                h.setCapacite(rs.getInt("capacite"));
                h.setPrix(rs.getInt("prix"));

                hebergementList.add(h);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Hebergements: " + e.getMessage());
        }
        return hebergementList;
    }
}
