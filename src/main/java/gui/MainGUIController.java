package gui;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainGUIController {
    private MainGUIController mainGUIController;

    public void setMainController(MainGUIController mainGUIController) {
        this.mainGUIController = mainGUIController;
    }

    @FXML
    private VBox sidebar;

    // 🔹 Ouvrir la gestion des missions
    @FXML
    private void ouvrirGestionMissions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/affmission.fxml"));
            Parent root = loader.load();

            AfficherMission afficherMissionController = loader.getController();
            afficherMissionController.setMainController(this); // Appel de la méthode ici ✅

            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Ouvrir la gestion des rapports
    @FXML
    private void ouvrirGestionRapports() {
        changerVue("/views/affrapport.fxml");
    }
    @FXML

    private void ouvrirAccueil() {
        changerVue("/views/mainGUI.fxml");
    }

    // 🔹 Ouvrir une fenêtre séparée (ex: affichage détaillé)
    private void ouvrirFenetre(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // 🔍 Debugging: Print full error message
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre: " + fxmlFile);
        }
    }

    // 🔹 Changer la vue principale
    private void changerVue(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue: " + fxml);
        }
    }

    // 🔹 Afficher une alerte en cas d'erreur
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 🔹 Quitter l'application
    @FXML
    private void quitterApplication() {
        System.exit(0);
    }
}
