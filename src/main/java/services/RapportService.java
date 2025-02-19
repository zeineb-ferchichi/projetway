package services;

import entities.Rapport;
import entities.RapportMissionDTO;
import entities.Mission;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RapportService implements IService<Rapport> {
    private final Connection conn = DBConnection.getInstance().getConn();

    @Override
    public void add(Rapport rapport) {
        System.out.println("Ressources à insérer : " + String.join(",", rapport.getRessources()));

        String req = "INSERT INTO rapport (libelleR, dateExpo, ressources, mission_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, rapport.getLibelleR());

            // ✅ Vérifier si la date n'est pas null avant de convertir
            if (rapport.getDateExpo() != null) {
                pst.setDate(2, Date.valueOf(rapport.getDateExpo()));
            } else {
                pst.setNull(2, Types.DATE);
            }

            pst.setString(3, String.join(",", rapport.getRessources()));
            pst.setInt(4, rapport.getMission().getIdMission());
            pst.executeUpdate();
            System.out.println("✅ Rapport ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du rapport : " + e.getMessage());
        }
    }

    @Override
    public List<Rapport> getAll() {
        List<Rapport> rapports = new ArrayList<>();
        String req = "SELECT * FROM rapport";
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                rapports.add(new Rapport(
                        rs.getInt("idRapport"),
                        rs.getString("libelleR"),
                        rs.getDate("dateExpo") != null ? rs.getDate("dateExpo").toLocalDate() : null, // ✅ Vérification null
                        Arrays.asList(rs.getString("ressources").split(",")), // ✅ Convertir en liste
                        new Mission(rs.getInt("mission_id"))
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des rapports : " + e.getMessage());
        }
        return rapports;
    }

    @Override
    public Rapport getById(int id) {
        String req = "SELECT * FROM rapport WHERE idRapport = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Rapport(
                        rs.getInt("idRapport"),
                        rs.getString("libelleR"),
                        rs.getDate("dateExpo") != null ? rs.getDate("dateExpo").toLocalDate() : null, // ✅ Vérification null
                        Arrays.asList(rs.getString("ressources").split(",")),
                        new Mission(rs.getInt("mission_id"))
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du rapport : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Rapport rapport) {
        String sql = "UPDATE rapport SET libelleR = ?, dateExpo = ?, ressources = ? WHERE idRapport = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rapport.getLibelleR());

            // ✅ Vérifier si la date n'est pas null avant de convertir
            if (rapport.getDateExpo() != null) {
                stmt.setDate(2, Date.valueOf(rapport.getDateExpo()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setString(3, rapport.getRessources().isEmpty() ? "" : String.join(",", rapport.getRessources()));
            stmt.setInt(4, rapport.getIdRapport());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Rapport mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun rapport mis à jour (ID introuvable) !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Échec de la mise à jour du rapport.");
        }
    }

    @Override
    public void delete(Rapport rapport) {
        String req = "DELETE FROM rapport WHERE idRapport = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, rapport.getIdRapport());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("🗑️ Rapport supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun rapport supprimé (ID introuvable) !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression du rapport : " + e.getMessage());
        }
    }

    public List<RapportMissionDTO> getAllRapportsAvecMissions() {
        List<RapportMissionDTO> rapports = new ArrayList<>();
        String req = "SELECT r.idRapport, r.libelleR, r.dateExpo, r.ressources, m.idMission, m.nomMission " +
                "FROM rapport r INNER JOIN mission m ON r.mission_id = m.idMission";

        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                rapports.add(new RapportMissionDTO(
                        rs.getInt("idRapport"),
                        rs.getString("libelleR"),
                        rs.getDate("dateExpo") != null ? rs.getDate("dateExpo").toLocalDate() : null, // ✅ Vérification null
                        Arrays.asList(rs.getString("ressources").split(",")),  // ✅ Convertit en liste
                        new Mission(rs.getInt("idMission"), rs.getString("nomMission"))
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des rapports avec missions : " + e.getMessage());
        }
        return rapports;
    }
}

