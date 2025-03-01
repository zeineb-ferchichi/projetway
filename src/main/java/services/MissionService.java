package services;

import entities.RapportMissionDTO;
import util.DBConnection;
import entities.Mission;
import entities.StatutTermint;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MissionService implements IService<Mission> {
    private static final Logger LOGGER = Logger.getLogger(MissionService.class.getName());
    private final Connection conn;

    public MissionService() {
        this.conn = DBConnection.getInstance().getConn();
    }

    @Override

    public void add(Mission mission) {
        String req = "INSERT INTO mission (nomMission, date_deb, date_fin, description, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, mission.getNomMission());
            pst.setDate(2, java.sql.Date.valueOf(mission.getDate_deb()));
            pst.setDate(3, java.sql.Date.valueOf(mission.getDate_fin()));
            pst.setString(4, mission.getdescription());
            pst.setString(5, mission.getStatut().name()); // Store ENUM as String

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mission.setIdMission(generatedKeys.getInt(1));
                    }
                }
                System.out.println("✅ Mission ajoutée avec statut: " + mission.getStatut());
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de la mission: " + e.getMessage());
        }
    }
    public List<Mission> getAllMissions() {
        List<Mission> missions = new ArrayList<>();
        String query = "SELECT idMission, nomMission, date_deb, date_fin, description, statut FROM mission";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mission mission = new Mission(
                        rs.getInt("idMission"),
                        rs.getString("nomMission"),
                        rs.getDate("date_deb").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getString("description"),
                        StatutTermint.valueOf(rs.getString("statut")) // Conversion String -> Enum
                );
                missions.add(mission);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des missions : " + e.getMessage());
        }
        return missions;
    }

    @Override
    public Mission getById(int id) {
        String req = "SELECT * FROM mission WHERE idMission = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Mission(
                        rs.getInt("idMission"),
                        rs.getString("nomMission"),
                        rs.getDate("date_deb").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getString("description"),
                        StatutTermint.valueOf(rs.getString("statut")) // Convert String to Enum
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de la mission: " + e.getMessage());
        }
        return null;
    }
    public void mettreAJourStatutMissions() {
        List<Mission> missions = getAll(); // Récupère toutes les missions
        LocalDate today = LocalDate.now();

        for (Mission mission : missions) {
            if (mission.getDate_deb().isAfter(today)) {
                mission.setStatut(StatutTermint.EN_ATTENTE);
            } else if (!mission.getDate_deb().isAfter(today) && !mission.getDate_fin().isBefore(today)) {
                mission.setStatut(StatutTermint.EN_COURS);
            } else {
                mission.setStatut(StatutTermint.TERMINE);
            }

            // ✅ Mise à jour dans la base de données
            update(mission);
        }
    }
    public void mettreAJourStatuts() {
        List<Mission> missions = getAll();
        LocalDate today = LocalDate.now();

        for (Mission mission : missions) {
            if (today.isAfter(mission.getDate_fin()) && mission.getStatut() != StatutTermint.TERMINE) {
                mission.setStatut(StatutTermint.TERMINE);
                update(mission);
            } else if ((today.isEqual(mission.getDate_deb()) || (today.isAfter(mission.getDate_deb()) && today.isBefore(mission.getDate_fin())))
                    && mission.getStatut() != StatutTermint.EN_COURS) {
                mission.setStatut(StatutTermint.EN_COURS);
                update(mission);
            }
        }
    }




    @Override
    public List<Mission> getAll() {
        List<Mission> missions = new ArrayList<>();
        String req = "SELECT * FROM mission";

        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(req)) {

            while (rs.next()) {
                try {
                    int id = rs.getInt("idMission");
                    String nom = rs.getString("nomMission");
                    LocalDate dateDebut = rs.getDate("date_deb").toLocalDate();
                    LocalDate dateFin = rs.getDate("date_fin").toLocalDate();
                    String description = rs.getString("description");
                    String statutString = rs.getString("statut");

                    // ✅ Vérification et conversion sécurisée du statut
                    StatutTermint statut;
                    try {
                        statut = StatutTermint.valueOf(statutString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("❌ Erreur : valeur de statut inconnue -> " + statutString);
                        statut = StatutTermint.EN_ATTENTE; // 🛑 Valeur par défaut pour éviter l'erreur
                    }

                    // ✅ Création de la mission et ajout à la liste
                    Mission mission = new Mission(id, nom, dateDebut, dateFin, description, statut);
                    missions.add(mission);

                    System.out.println("✅ Mission chargée : " + mission);

                } catch (Exception e) {
                    System.err.println("❌ Erreur lors de la conversion d'une mission : " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la récupération des missions : " + e.getMessage());
        }

        System.out.println("🔍 Nombre total de missions récupérées : " + missions.size());
        return missions;
    }


    @Override
    public void delete(Mission mission) {
        String req = "DELETE FROM mission WHERE idMission = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, mission.getIdMission());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                LOGGER.info("🗑️ Mission supprimée avec succès !");
            } else {
                LOGGER.warning("⚠️ Aucune mission trouvée avec l'ID : " + mission.getIdMission());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "❌ Erreur lors de la suppression de la mission", e);
        }
    }

    public void update(Mission mission) {
        String req = "UPDATE mission SET nomMission = ?, description = ?, date_deb = ?, date_fin = ?, statut = ? WHERE idMission = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, mission.getNomMission());
            pst.setString(2, mission.getdescription());
            pst.setDate(3, java.sql.Date.valueOf(mission.getDate_deb()));
            pst.setDate(4, java.sql.Date.valueOf(mission.getDate_fin()));
            pst.setString(5, mission.getStatut().toString()); // ✅ Vérifie que le statut est bien mis à jour
            pst.setInt(6, mission.getIdMission());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Mission mise à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucune ligne mise à jour !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de la mission : " + e.getMessage());
        }
    }
    public void updateStatutMissions() {
        List<Mission> missions = getAll();
        LocalDate today = LocalDate.now();

        for (Mission mission : missions) {
            if (today.isAfter(mission.getDate_deb()) && today.isBefore(mission.getDate_fin())) {
                mission.setStatut(StatutTermint.EN_COURS);
            } else if (today.isAfter(mission.getDate_fin())) {
                mission.setStatut(StatutTermint.TERMINE);
            }
            update(mission); // ✅ Mise à jour de la mission dans la base de données
        }
    }

}
