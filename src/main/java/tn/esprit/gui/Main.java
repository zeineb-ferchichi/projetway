package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main {



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
