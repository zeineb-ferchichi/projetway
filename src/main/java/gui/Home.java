package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Home {

    @FXML private ImageView sidebarImage;
    @FXML private Button btnAbonnement, btnTransport;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/sidebar.jpg")));
            sidebarImage.setImage(image);
        } catch (Exception e) {
            System.out.println("Erreur: Impossible de charger l'image du sidebar.");
        }
    }

    @FXML
    private void goAbonnement() {
        loadScene("Abonnement.fxml");
    }

    @FXML
    private void goTransport() {
        loadScene("Transport.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) btnAbonnement.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de chargement du fichier : " + fxmlFile);
        }
    }
}
