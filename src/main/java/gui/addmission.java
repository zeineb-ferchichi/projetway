package gui;

import entities.Mission;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
    private DatePicker DPDateFin;
    @FXML
    private ComboBox<String> cbStatut;

    private final MissionService missionService = new MissionService();

    @FXML
    private void ajouterMission() {
        String nom = TFNomMission.getText();
        String description = TFDescription.getText();
        LocalDate dateDebut = DPDateDebut.getValue();
        LocalDate dateFin = DPDateFin.getValue();
        String selectedStatut = cbStatut.getValue(); // Get selected status

        if (nom.isEmpty() || description.isEmpty() || dateDebut == null || dateFin == null || selectedStatut == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Convert String to Enum (if using Enum)
        StatutTermint statut = StatutTermint.valueOf(selectedStatut);

        // Create and save mission
        Mission newMission = new Mission(0, nom, dateDebut, dateFin, description, statut);
        missionService.add(newMission);

        showAlert("Succès", "✅ Mission ajoutée avec statut: " + statut);
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

