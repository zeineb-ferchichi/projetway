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
        String SQL = "INSERT INTO Hebergement (nom, type, adresse, ville, pays, capacite, prix, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
            pstmt.setString(8, hebergement.getImage()); // Ajout de l'image

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Hebergement added successfully!");
            } else {
                System.out.println("Failed to add Hebergement.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding Hebergement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Hebergement hebergement) {
        String SQL = "UPDATE Hebergement SET nom = ?, type = ?, adresse = ?, ville = ?, pays = ?, capacite = ?, prix = ?, image = ? WHERE id = ?";

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
            pstmt.setString(8, hebergement.getImage()); // Mise à jour de l'image
            pstmt.setInt(9, hebergement.getId());

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
                h.setImage(rs.getString("image")); // Récupération de l'image

                hebergementList.add(h);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Hebergements: " + e.getMessage());
        }
        return hebergementList;
    }

    /**
     * Méthode ajoutée pour récupérer un hébergement par ID.
     * @param id l'identifiant de l'hébergement
     * @return l'objet Hebergement correspondant, ou null s'il n'existe pas
     */
    public Hebergement getById(int id) {
        String SQL = "SELECT * FROM Hebergement WHERE id = ?";

        if (conn == null) {
            System.out.println("Error: Connection is null.");
            return null;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getString("adresse"),
                        rs.getString("ville"),
                        rs.getString("pays"),
                        rs.getInt("capacite"),
                        rs.getInt("prix"),
                        rs.getString("image") // Ajout de l'image
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Hebergement by ID: " + e.getMessage());
        }
        return null;
    }
}
