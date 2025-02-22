package services;

import models.Abonnement;
import models.AbonnementTransportDTO;
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

    /**
     * Vérifie si un transport existe avant de l'associer à un abonnement.
     */
    public boolean checkTransportExists(int transportId) {
        String sql = "SELECT COUNT(*) FROM transport WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transportId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la vérification du transport_id", e);
        }
        return false;
    }

    @Override
    public void add(Abonnement abonnement) {
        System.out.println("🔍 Vérification de transport_id: " + abonnement.getTransport_id());

        if (!checkTransportExists(abonnement.getTransport_id())) {
            System.out.println("❌ Erreur : transport_id invalide !");
            return;
        }

        // Vérification de la validité de la durée
        if (!isDureeValableValid(abonnement.getDuree_valable(), abonnement.getTransport_id())) {
            System.out.println("❌ Erreur : La durée valable dépasse la durée du transport !");
            return;
        }

        String sql = "INSERT INTO abonnement (type_abonnem, montant, duree_valable, status_abonnem, transport_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, abonnement.getType_abonnem());
            stmt.setDouble(2, abonnement.getMontant());
            stmt.setInt(3, abonnement.getDuree_valable());
            stmt.setString(4, abonnement.getStatus_abonnem());
            stmt.setInt(5, abonnement.getTransport_id());
            stmt.executeUpdate();
            System.out.println("✅ Abonnement ajouté avec succès.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ajout d'un abonnement", e);
        }
    }

    public void update(Abonnement abonnement) {
        String sql = "UPDATE abonnement SET type_abonnem = ?, montant = ?, duree_valable = ?, status_abonnem = ?, transport_id = ? WHERE id_abonnem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, abonnement.getType_abonnem());
            stmt.setDouble(2, abonnement.getMontant());
            stmt.setInt(3, abonnement.getDuree_valable());
            stmt.setString(4, abonnement.getStatus_abonnem());
            stmt.setInt(5, abonnement.getTransport_id());
            stmt.setInt(6, abonnement.getId_abonnem());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Abonnement mis à jour avec succès.");
            } else {
                System.out.println("❌ Échec de la mise à jour : ID introuvable.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Abonnement abonnement) {
        String sql = "DELETE FROM abonnement WHERE id_abonnem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, abonnement.getId_abonnem());
            stmt.executeUpdate();
            System.out.println("✅ Abonnement supprimé avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
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

    /**
     * Vérifie que la durée valable d’un abonnement ne dépasse pas la durée du transport associé.
     */
    public boolean isDureeValableValid(int dureeValable, int transportId) {
        String sql = "SELECT duree_max FROM transport WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transportId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int dureeMax = rs.getInt("duree_max");
                    return dureeValable <= dureeMax;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la vérification de la durée valable", e);
        }
        return false;
    }

    private Abonnement extractAbonnement(ResultSet rs) throws SQLException {
        Abonnement abonnement = new Abonnement();
        abonnement.setId_abonnem(rs.getInt("id_abonnem"));
        abonnement.setType_abonnem(rs.getString("type_abonnem"));
        abonnement.setMontant(rs.getDouble("montant"));
        abonnement.setDuree_valable(rs.getInt("duree_valable"));
        abonnement.setStatus_abonnem(rs.getString("status_abonnem"));
        abonnement.setTransport_id(rs.getInt("transport_id"));
        return abonnement;
    }
}
