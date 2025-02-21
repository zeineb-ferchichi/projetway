package services;

import models.Abonnement;
import models.AbonnementTransportDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
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

        String sql = "INSERT INTO abonnement (type_abonnem, montant, date_debut, date_fin, transport_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            setAbonnementParams(stmt, abonnement);
            stmt.executeUpdate();
            System.out.println("✅ Abonnement ajouté avec succès.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ajout d'un abonnement", e);
        }
    }

    public void update(Abonnement abonnement) {
        String sql = "UPDATE abonnement SET type_abonnem = ?, montant = ?, date_debut = ?, date_fin = ?, transport_id = ? WHERE id_abonnem = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, abonnement.getType_abonnem());
            stmt.setDouble(2, abonnement.getMontant());
            stmt.setDate(3, new java.sql.Date(abonnement.getDate_debut().getTime()));
            stmt.setDate(4, new java.sql.Date(abonnement.getDate_fin().getTime()));
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
     * Récupère la liste des abonnements avec leur transport associé.
     */
    public List<AbonnementTransportDTO> getAbonnementTransport() {
        String sql = "SELECT a.id_abonnem, a.type_abonnem, a.montant, a.date_debut, a.date_fin, " +
                "t.id_transp, t.type_transp " +
                "FROM abonnement a " +
                "JOIN transport t ON a.transport_id = t.id_transp";

        List<AbonnementTransportDTO> resultList = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resultList.add(extractAbonnementTransport(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des abonnements avec transport", e);
        }
        return resultList;
    }

    /**
     * Factorisation pour extraire un abonnement depuis un ResultSet.
     */
    private Abonnement extractAbonnement(ResultSet rs) throws SQLException {
        Abonnement abonnement = new Abonnement();
        abonnement.setId_abonnem(rs.getInt("id_abonnem"));
        abonnement.setType_abonnem(rs.getString("type_abonnem"));
        abonnement.setMontant(rs.getDouble("montant"));
        abonnement.setDate_debut(rs.getDate("date_debut"));
        abonnement.setDate_fin(rs.getDate("date_fin"));
        abonnement.setTransport_id(rs.getInt("transport_id"));
        return abonnement;
    }

    /**
     * Factorisation pour extraire un DTO d'abonnement transport.
     */
    private AbonnementTransportDTO extractAbonnementTransport(ResultSet rs) throws SQLException {
        AbonnementTransportDTO dto = new AbonnementTransportDTO();
        dto.setIdAbonnem(rs.getInt("id_abonnem"));
        dto.setTypeAbonnem(rs.getString("type_abonnem"));
        dto.setMontant(rs.getDouble("montant"));
        dto.setDateDebut(rs.getDate("date_debut"));
        dto.setDateFin(rs.getDate("date_fin"));
        dto.setIdTransp(rs.getInt("id_transp"));
        dto.setTypeTransport(rs.getString("type_transp"));
        return dto;
    }

    /**
     * Factorisation pour définir les paramètres communs à un abonnement.
     */
    private void setAbonnementParams(PreparedStatement stmt, Abonnement abonnement) throws SQLException {
        stmt.setString(1, abonnement.getType_abonnem());
        stmt.setDouble(2, abonnement.getMontant());
        stmt.setDate(3, new java.sql.Date(abonnement.getDate_debut().getTime()));
        stmt.setDate(4, new java.sql.Date(abonnement.getDate_fin().getTime()));
        stmt.setInt(5, abonnement.getTransport_id());
    }
}
