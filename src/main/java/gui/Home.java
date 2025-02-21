package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class Home {

    @FXML
    private ImageView logoImageView;
    @FXML
    private Button btnAbonnement, btnTransport;

    @FXML
    public void initialize() {
        // Chargement du logo
        try {
            Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/logo.png")));
            logoImageView.setImage(logo);
        } catch (Exception e) {
            System.out.println("Erreur : Impossible de charger le logo.");
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
            e.printStackTrace();
            System.out.println("Erreur de chargement du fichier FXML : " + fxmlFile);
        }
    }
}
