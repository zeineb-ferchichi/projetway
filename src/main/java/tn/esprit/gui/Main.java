package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class Main {

    @FXML
    private ImageView imageView;  // Reference to the ImageView

    @FXML
    private Text welcomeText;  // Reference to the welcome text

    @FXML
    private void initialize() {
        // Ensure the welcomeText is initialized before trying to set its value
        if (welcomeText != null) {
            welcomeText.setText("Welcome to Projetway!");
        }

        // Load image for the right side of the layout
        loadImage();
    }

    private void loadImage() {
        // Define the path to the image you want to display
        String imagePath = "C:\\Users\\khali\\IdeaProjects\\GestionHebrgement\\474208728_1084367110107407_4535390598455481204_n.png";
        Image image = new Image(imagePath);
        imageView.setImage(image);
    }

    @FXML
    private void AfficherHebergement() {
        ouvrirFenetre("/AfficherHebergement.fxml", "Afficher les Hébergements");
    }

    @FXML
    private void AfficherReservation() {
        ouvrirFenetre("/AfficherReservation.fxml", "Afficher les Réservations");
    }

    @FXML
    private void quitter() {
        System.exit(0);
    }

    private void ouvrirFenetre(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(titre);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
