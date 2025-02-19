package gui;

import entities.Mission;
import entities.StatutTermint;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.MissionService;
import java.io.IOException;
import java.util.List;

public class AfficherMission {

    @FXML
    private GridPane gridMissions;

    private final MissionService missionService = new MissionService();

    @FXML
    private void initialize() {
        loadMissions(); // Charger la liste des missions au démarrage
    }

    private void loadMissions() {
        gridMissions.getChildren().clear(); // Nettoyer avant d'afficher les nouvelles missions

        List<Mission> missions = missionService.getAll();
        int row = 1;

        for (Mission mission : missions) {
            gridMissions.add(new Label(String.valueOf(mission.getIdMission())), 0, row);
            gridMissions.add(new Label(mission.getNomMission()), 1, row);
            gridMissions.add(new Label(mission.getDate_deb().toString()), 2, row);
            gridMissions.add(new Label(mission.getDate_fin().toString()), 3, row);
            gridMissions.add(new Label(mission.getdescription()), 4, row);
            gridMissions.add(new Label(mission.getStatut().toString()), 5, row);

            // ✅ Bouton Modifier avec une icône
            Button modifyButton = new Button("✏ Modifier");
            modifyButton.setOnAction(event -> ouvrirFenetreModification(mission));
            gridMissions.add(modifyButton, 6, row);

            // ✅ Bouton Supprimer avec une icône
            Button deleteButton = new Button("🗑 Supprimer");
            deleteButton.setOnAction(event -> supprimerMission(mission));
            gridMissions.add(deleteButton, 7, row);

            row++;
        }
    }

    private void ouvrirFenetreModification(Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierMission.fxml"));
            Parent root = loader.load();

            ModifierMission controller = loader.getController();
            controller.setMissionData(mission);

            Stage stage = new Stage();
            stage.setTitle("Modifier Mission");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }

    private void supprimerMission(Mission mission) {
        missionService.delete(mission);
        showAlert("Succès", "Mission supprimée avec succès !");
        loadMissions(); // Rafraîchir la liste après suppression
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void refreshMissions() {
        loadMissions();  // 🔄 Recharge les missions affichées
        System.out.println("🔄 Rafraîchissement des missions...");
    }
    @FXML
    private void ouvrirAjoutMission() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addmission.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Mission");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout !");
        }
    }

}
