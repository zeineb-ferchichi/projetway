package gui;

import entities.Mission;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.MissionService;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import entities.StatutTermint;
import java.time.LocalDate;

public class addmission {

    @FXML
    private TextField TFNomMission;

    @FXML
    private TextField TFDescription;

    @FXML
    private DatePicker DPDateDebut;
    @FXML
    private Button btnAjouter;

    @FXML
    private DatePicker DPDateFin;
    @FXML
    private ComboBox<String> cbStatut;

    private final MissionService missionService = new MissionService();


    @FXML
    private void ajouterMission() {
        String nomMission = TFNomMission.getText().trim();
        String description = TFDescription.getText().trim();
        LocalDate dateDebut = DPDateDebut.getValue();
        LocalDate dateFin = DPDateFin.getValue();

        if (nomMission.isEmpty() || description.isEmpty() || dateDebut == null || dateFin == null) {
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
        StatutTermint statut;
        LocalDate today = LocalDate.now();

        if (today.isBefore(dateDebut)) {
            statut = StatutTermint.EN_ATTENTE; // Avant la date de début
        } else if (today.isEqual(dateDebut) || (today.isAfter(dateDebut) && today.isBefore(dateFin))) {
            statut = StatutTermint.EN_COURS; // Pendant la période de la mission
        } else {
            statut = StatutTermint.TERMINE; // Après la date de fin
        }




        Mission nouvelleMission = new Mission(nomMission, dateDebut, dateFin, description, statut);
        missionService.add(nouvelleMission);

        showAlert("Succès", "✅ Mission ajoutée avec succès !");
// ✅ Rafraîchir automatiquement la liste dans AfficherMission
        if (AfficherMission.afficherMissionsInstance != null) {
            AfficherMission.afficherMissionsInstance.rafraichirListeMissions();
        } else {
            System.out.println("⚠ AfficherMission n'est pas initialisé !");
        }

// ✅ Fermer la fenêtre après l'ajout
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void afficherMissions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/affmission.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Liste des Missions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de l'ouverture de la fenêtre d'affichage : " + e.getMessage());
        }

    }

}

