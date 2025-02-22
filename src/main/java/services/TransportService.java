package services;

import models.Transport;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportService {
    private final Connection conn;

    public TransportService() {
        this.conn = DBConnection.getInstance().getConn();
    }

    public void add(Transport transport) {
        String sql = "INSERT INTO transport (type_transp, nom_station, zone_geographique) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transport.getType_transp());
            stmt.setString(2, transport.getNom_station());
            stmt.setString(3, transport.getZone_geographique());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Transport transport) {
        String sql = "UPDATE transport SET type_transp = ?, nom_station = ?, zone_geographique = ? WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transport.getType_transp());
            stmt.setString(2, transport.getNom_station());
            stmt.setString(3, transport.getZone_geographique());
            stmt.setInt(4, transport.getId_transp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(int id) { // Changed method signature to accept int
        String sql = "DELETE FROM transport WHERE id_transp = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; // Returns true if deletion is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
