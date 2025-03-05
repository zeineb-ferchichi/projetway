package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class interfaces {

    @FXML
    private void gererVoyage(ActionEvent event) {
        ouvrirFenetre("/ajoutervoyage.fxml", "Gérer Voyage");
    }

    @FXML
    private void gererTrajet(ActionEvent event) {
        ouvrirFenetre("/ajoutertrajet.fxml", "Gérer Trajet");
    }

    @FXML
    private void affichertables(ActionEvent event) {
        ouvrirFenetre("/affichertables.fxml", "Afficher Voyages");
    }
    @FXML
    private void afficherVoyage(ActionEvent event) {
        ouvrirFenetre("/affichervoyage.fxml", "Afficher Voyages");
    }
    @FXML
    private void affichertrajet(ActionEvent event) {
        ouvrirFenetre("/affichertrajet.fxml", "Affichertrajet");
    }
    @FXML
    private void affichertrajetss(ActionEvent event) {
        ouvrirFenetre("/affichervoy.fxml", "Affichervoyage");
    }
    private void ouvrirFenetre(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent root = loader.load()
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la fenêtre: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
