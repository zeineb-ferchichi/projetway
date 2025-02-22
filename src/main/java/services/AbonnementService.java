package services;

import models.Abonnement;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbonnementService implements IService<Abonnement> {
    private final Connection conn;
    private static final Logger logger = Logger.getLogger(AbonnementService.class.getName());

    public AbonnementService() {
        this.conn = DBConnection.getInstance().getConn();
    }

    @Override
    public void add(Abonnement abonnement) {
        String sql = "INSERT INTO abonnement (type_abonnem, montant, duree_valable, status_abonnem, transport_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, abonnement.getType_abonnem());
            stmt.setDouble(2, abonnement.getMontant());
            stmt.setInt(3, abonnement.getDuree_valable());
            stmt.setString(4, abonnement.getStatus_abonnem());
            stmt.setInt(5, abonnement.getTransport_id());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Nombre de lignes insérées : " + rowsAffected);
            if (rowsAffected > 0) {
                System.out.println("✅ Abonnement ajouté avec succès.");
            } else {
                System.out.println("❌ Échec de l'ajout.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public boolean delete(int id) { // ✅ Correction ici
        String sql = "DELETE FROM abonnement WHERE id_abonnem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la suppression d'un abonnement", e);
            return false;
        }
    }

    @Override
    public boolean update(Abonnement abonnement) { // ✅ Correction pour respecter `IService`
        if (abonnement == null || abonnement.getId_abonnem() == 0) return false;

        String sql = "UPDATE abonnement SET type_abonnem = ?, montant = ?, duree_valable = ?, status_abonnem = ?, transport_id = ? WHERE id_abonnem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, abonnement.getType_abonnem());
            stmt.setDouble(2, abonnement.getMontant());
            stmt.setInt(3, abonnement.getDuree_valable());
            stmt.setString(4, abonnement.getStatus_abonnem());
            stmt.setInt(5, abonnement.getTransport_id());
            stmt.setInt(6, abonnement.getId_abonnem());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la mise à jour de l'abonnement", e);
            return false;
        }
    }

    @Override
    public List<Abonnement> getAll() {
        List<Abonnement> list = new ArrayList<>();
        String sql = "SELECT * FROM abonnement";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractAbonnement(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des abonnements", e);
        }
        return list;
    }

    @Override
    public Abonnement getById(int id) {
        String sql = "SELECT * FROM abonnement WHERE id_abonnem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAbonnement(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération d'un abonnement par ID", e);
        }
        return null;
    }

    private Abonnement extractAbonnement(ResultSet rs) throws SQLException { // ✅ Correction ici
        return new Abonnement(
                rs.getInt("id_abonnem"),
                rs.getString("type_abonnem"),
                rs.getDouble("montant"),
                rs.getInt("duree_valable"),
                rs.getInt("transport_id"), // ✅ Transport ID avant Status
                rs.getString("status_abonnem") // ✅ Status à la fin
        );
    }
}
