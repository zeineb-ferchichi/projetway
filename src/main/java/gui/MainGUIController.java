package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainGUIController {

    @FXML
    private void ouvrirGestionMissions() {
        ouvrirFenetre("/views/affmission.fxml", "Gestion des Missions");
    }

    @FXML
    private void ouvrirGestionRapports() throws IOException {
        ouvrirFenetre("/views/affrapport.fxml", "gerez vos rapports");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

        @FXML
    private void quitterApplication() {
        System.exit(0);
    }

    @FXML
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

}
