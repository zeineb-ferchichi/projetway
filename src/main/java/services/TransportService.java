package services;

import models.Transport;
import util.DBConnection;
import util.WindowsNotificationUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportService {
    private final Connection conn;

    public TransportService() {
        this.conn = DBConnection.getInstance().getConn();
    }

    public int add(Transport transport) {
        String sql = "INSERT INTO transport (type_transp, nom_station, zone_geographique) VALUES (?, ?, ?)";
        int generatedId = -1; // ✅ Variable pour stocker l'ID généré

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, transport.getType_transp());
            stmt.setString(2, transport.getNom_station());
            stmt.setString(3, transport.getZone_geographique());
            stmt.executeUpdate();

            // ✅ Récupération de l'ID généré
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

            // 🔔 Notification Windows avec ID
            WindowsNotificationUtil.showWindowsNotification("Ajout Transport", "Transport ID: " + generatedId + " ajouté !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId; // ✅ Retourner l'ID généré
    }


    public void update(Transport transport) {
        String sql = "UPDATE transport SET type_transp = ?, nom_station = ?, zone_geographique = ? WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transport.getType_transp());
            stmt.setString(2, transport.getNom_station());
            stmt.setString(3, transport.getZone_geographique());
            stmt.setInt(4, transport.getId_transp());
            stmt.executeUpdate();

            // 🔔 Notification Windows
            WindowsNotificationUtil.showWindowsNotification("Modification Transport", "Le transport " + transport.getNom_station() + " a été modifié !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM transport WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            boolean success = stmt.executeUpdate() > 0;

            if (success) {
                // 🔔 Notification Windows
                WindowsNotificationUtil.showWindowsNotification("Suppression Transport", "Un transport a été supprimé !");
            }

            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Transport getById(int id) {
        String sql = "SELECT * FROM transport WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Transport(
                        rs.getInt("id_transp"),
                        rs.getString("type_transp"),
                        rs.getString("nom_station"),
                        rs.getString("zone_geographique")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transport> getAll() {
        List<Transport> list = new ArrayList<>();
        String sql = "SELECT * FROM transport";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Transport(
                        rs.getInt("id_transp"),
                        rs.getString("type_transp"),
                        rs.getString("nom_station"),
                        rs.getString("zone_geographique")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
