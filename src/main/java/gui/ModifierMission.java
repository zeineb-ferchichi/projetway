package gui;

import entities.Mission;
import entities.StatutTermint;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.MissionService;

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
    private ComboBox<String> cbStatut;
    @FXML
    private Button btnUpdate;

    private final MissionService missionService = new MissionService();
    private Mission currentMission;

    // ✅ Méthode qui reçoit une mission et remplit les champs du formulaire
    public void setMission(Mission mission) {
        this.currentMission = mission;
        if (mission != null) {
            remplirChamps();
        }
    }

    // ✅ Remplissage des champs
    private void remplirChamps() {
        TFNomMission.setText(currentMission.getNomMission());
        TFDescription.setText(currentMission.getdescription());
        DPDateDebut.setValue(currentMission.getDate_deb());
        DPDateFin.setValue(currentMission.getDate_fin());
        cbStatut.setValue(currentMission.getStatut().toString());
    }

    // ✅ Mettre à jour la mission
    @FXML
    private void updateMission() {
        String nom = TFNomMission.getText().trim();
        String description = TFDescription.getText().trim();
        LocalDate dateDebut = DPDateDebut.getValue();
        LocalDate dateFin = DPDateFin.getValue();

        if (nom.isEmpty() || description.isEmpty() || dateDebut == null || dateFin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (dateDebut.isAfter(dateFin)) {
            showAlert("Erreur", "La date de début doit être avant la date de fin !");
            return;
        }

        // ✅ Mise à jour automatique du statut
        StatutTermint statut;
        if (LocalDate.now().isBefore(dateDebut)) {
            statut = StatutTermint.EN_ATTENTE;
        } else if (LocalDate.now().isAfter(dateFin)) {
            statut = StatutTermint.TERMINE;
        } else {
            statut = StatutTermint.EN_COURS;
        }

        currentMission.setNomMission(nom);
        currentMission.setdescription(description);
        currentMission.setDate_deb(dateDebut);
        currentMission.setDate_fin(dateFin);
        currentMission.setStatut(statut);

        missionService.update(currentMission);
        showAlert("Succès", "✅ Mission mise à jour avec succès !");

        // ✅ Fermeture automatique de la fenêtre
        Stage stage = (Stage) btnUpdate.getScene().getWindow();
        stage.close();

        // ✅ Rafraîchir la liste des missions
        if (AfficherMission.afficherMissionsInstance != null) {
            AfficherMission.afficherMissionsInstance.rafraichirListeMissions();
        }
    }

    // ✅ Affichage d’une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
