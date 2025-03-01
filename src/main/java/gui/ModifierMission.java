package gui;

import entities.Mission;
import entities.StatutTermint;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.MissionService;
import util.InputValidation;

import java.time.LocalDate;

public class ModifierMission {

    @FXML
    private TextField TFNomMission;
    @FXML
    private TextField TFDescription;
    @FXML
    private DatePicker DPDateDebut;
    @FXML
    private DatePicker DPDateFin;
    @FXML
    private Button btnUpdate;

    private final MissionService missionService = new MissionService();
    private Mission currentMission;



    public void setMissionData(Mission mission) {
        this.currentMission = mission;
        TFNomMission.setText(mission.getNomMission());
        TFDescription.setText(mission.getdescription());
        DPDateDebut.setValue(mission.getDate_deb());
        DPDateFin.setValue(mission.getDate_fin());

        System.out.println("🔍 Mission chargée : " + mission.getNomMission() + ", Statut : " + mission.getStatut());
    }

    @FXML
    private void updateMission() {
        if (currentMission == null) {
            showAlert("Erreur", "Aucune mission sélectionnée !");
            return;
        }

        String nom = TFNomMission.getText().trim();
        String description = TFDescription.getText().trim();
        LocalDate dateDebut = DPDateDebut.getValue();
        LocalDate dateFin = DPDateFin.getValue();

        if (nom.isEmpty() || description.isEmpty() || dateDebut == null || dateFin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (dateDebut.isBefore(LocalDate.now())) {
            showAlert("Erreur", "La date de début ne peut pas être dans le passé !");
            return;
        }

        if (dateDebut.isAfter(dateFin)) {
            showAlert("Erreur", "La date de début doit être avant la date de fin !");
            return;
        }

        StatutTermint statut = StatutTermint.EN_ATTENTE;
        if (LocalDate.now().isAfter(dateDebut) && LocalDate.now().isBefore(dateFin)) {
            statut = StatutTermint.EN_COURS;
        } else if (LocalDate.now().isAfter(dateFin)) {
            statut = StatutTermint.TERMINE;
        }

        currentMission.setNomMission(nom);
        currentMission.setdescription(description);
        currentMission.setDate_deb(dateDebut);
        currentMission.setDate_fin(dateFin);
        currentMission.setStatut(statut);

        missionService.update(currentMission);
        showAlert("Succès", "✅ Mission mise à jour avec succès !");

        Stage stage = (Stage) btnUpdate.getScene().getWindow();
        stage.close();

        // ✅ Rafraîchir la liste des missions
        AfficherMission.afficherMissionsInstance.rafraichirListeMissions();
    }
    private Mission mission;
    @FXML
    private ComboBox<String> cbStatut;

    public void setMission(Mission mission) {
        this.mission = mission;
        remplirChamps();  // ✅ Ajoutez cette ligne pour remplir les champs du formulaire
    }
    private void remplirChamps() {
        if (mission != null) {
            TFNomMission.setText(mission.getNomMission());
            TFDescription.setText(mission.getdescription());
            DPDateDebut.setValue(mission.getDate_deb());
            DPDateFin.setValue(mission.getDate_fin());
            cbStatut.setValue(mission.getStatut().toString());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
