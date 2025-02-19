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
    private ComboBox<StatutTermint> cbStatut;
    @FXML
    private Button btnUpdate;

    private final MissionService missionService = new MissionService();
    private Mission currentMission;@FXML
    private void initialize() {
        cbStatut.setItems(FXCollections.observableArrayList(StatutTermint.values())); // Charger les valeurs
        cbStatut.setDisable(false); // Activer la modification
        cbStatut.setEditable(false); // Empêcher l'écriture manuelle
        cbStatut.setPromptText("Sélectionner un statut"); // Ajoute un texte indicatif
    }


    public void setMissionData(Mission mission) {
        this.currentMission = mission;
        TFNomMission.setText(mission.getNomMission());
        TFDescription.setText(mission.getdescription());
        DPDateDebut.setValue(mission.getDate_deb());
        DPDateFin.setValue(mission.getDate_fin());
        cbStatut.setValue(mission.getStatut()); // ✅ Sélection du statut

        System.out.println("🔍 Mission chargée  " + mission.getNomMission() + ", Statut : " + mission.getStatut());
    }

    @FXML
    private void updateMission() {
        if (currentMission == null) {
            showAlert("Erreur", "Aucune mission sélectionnée !");
            return;
        }

        // Récupération des valeurs
        String nom = TFNomMission.getText().trim();
        String description = TFDescription.getText().trim();
        LocalDate dateDebut = DPDateDebut.getValue();
        LocalDate dateFin = DPDateFin.getValue();
        StatutTermint statut = cbStatut.getValue(); // Vérifie si un statut est sélectionné

        // Vérification des champs
        if (nom.isEmpty() || description.isEmpty() || dateDebut == null || dateFin == null || statut == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        System.out.println("🔄 Mise à jour Mission : " + nom + " | Nouveau statut : " + statut);

        // Mise à jour des valeurs
        currentMission.setNomMission(nom);
        currentMission.setdescription(description);
        currentMission.setDate_deb(dateDebut);
        currentMission.setDate_fin(dateFin);
        currentMission.setStatut(statut);

        // Mise à jour dans la base de données
        missionService.update(currentMission);
        showAlert("Succès", "✅ Mission mise à jour avec succès !");

        // Fermer la fenêtre après mise à jour
        Stage stage = (Stage) btnUpdate.getScene().getWindow();
        stage.close();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
