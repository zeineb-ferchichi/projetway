package services;

import models.Transport;
import util.DBConnection;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportService implements IService<Transport> {

    private final Connection conn;
    private static final Logger logger = Logger.getLogger(TransportService.class.getName());

    public TransportService() {
        // Récupérer la connexion depuis DBConnection
        conn = DBConnection.getInstance().getConn();
    }

    @Override
    public void add(Transport transport) {
        if (conn == null) {
            logger.log(Level.WARNING, "⚠ Connexion MySQL nulle : impossible d'ajouter un transport !");
            return;
        }
        String SQL = "INSERT INTO transport (type_transp, nom_station, zone_geographique) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setString(1, transport.getType_transp());
            stmt.setString(2, transport.getNom_station());
            stmt.setString(3, transport.getZone_geographique());
            stmt.executeUpdate();
            logger.log(Level.INFO, "✅ Transport ajouté avec succès !");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Erreur lors de l'ajout du transport : " + e.getMessage(), e);
        }
    }

    /**
     * Met à jour tous les champs du transport (type_transp, nom_station, zone_geographique)
     * où l'ID correspond à transport.getId_transp().
     */
    public void update(Transport transport) {
        if (conn == null) {
            logger.log(Level.WARNING, "⚠ Connexion MySQL nulle : impossible de mettre à jour un transport !");
            return;
        }
        String sql = "UPDATE transport SET type_transp = ?, nom_station = ?, zone_geographique = ? WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transport.getType_transp());
            stmt.setString(2, transport.getNom_station());
            stmt.setString(3, transport.getZone_geographique());
            stmt.setInt(4, transport.getId_transp());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "✅ Transport ID {0} mis à jour avec succès.", transport.getId_transp());
            } else {
                logger.log(Level.WARNING, "❌ Échec de la mise à jour : ID {0} introuvable.", transport.getId_transp());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Erreur lors de la mise à jour du transport", e);
        }
    }

    @Override
    public void delete(Transport transport) {
        if (conn == null) {
            logger.log(Level.WARNING, "⚠ Connexion MySQL nulle : impossible de supprimer un transport !");
            return;
        }
        String SQL = "DELETE FROM transport WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, transport.getId_transp());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.log(Level.INFO, "✅ Transport ID {0} supprimé avec succès.", transport.getId_transp());
            } else {
                logger.log(Level.WARNING, "❌ Transport ID {0} introuvable, suppression impossible.", transport.getId_transp());
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Erreur lors de la suppression du transport : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transport> getAll() {
        List<Transport> transports = new ArrayList<>();
        if (conn == null) {
            logger.log(Level.WARNING, "⚠ Connexion MySQL nulle : impossible de récupérer les transports !");
            return transports;
        }
        String sql = "SELECT * FROM transport";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Transport t = new Transport();
                t.setId_transp(rs.getInt("id_transp"));
                t.setType_transp(rs.getString("type_transp"));
                t.setNom_station(rs.getString("nom_station"));
                t.setZone_geographique(rs.getString("zone_geographique"));
                transports.add(t);
            }
            logger.log(Level.INFO, "✅ Liste des transports récupérée avec succès.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Erreur lors de la récupération des transports", e);
        }
        return transports;
    }

    @Override
    public Transport getById(int id) {
        if (conn == null) {
            logger.log(Level.WARNING, "⚠ Connexion MySQL nulle : impossible de récupérer un transport par ID !");
            return null;
        }
        String SQL = "SELECT * FROM transport WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Transport transport = new Transport();
                    transport.setId_transp(rs.getInt("id_transp"));
                    transport.setType_transp(rs.getString("type_transp"));
                    transport.setNom_station(rs.getString("nom_station"));
                    transport.setZone_geographique(rs.getString("zone_geographique"));
                    return transport;
                } else {
                    logger.log(Level.WARNING, "⚠ Transport ID {0} introuvable.", id);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Erreur lors de la récupération du transport par ID : " + e.getMessage(), e);
        }
        return null;
    }
}
