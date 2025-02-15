package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args); // Démarre l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterForum.fxml"));

        try {
            // Charger le contenu de l'interface graphique depuis le FXML
            Parent root = loader.load();

            // Créer une scène et la lier au stage principal
            Scene scene = new Scene(root);

            // Configurer le titre de la fenêtre
            primaryStage.setTitle("Ajouter Forum");

            // Appliquer la scène au stage et afficher la fenêtre
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            // Afficher un message d'erreur si le fichier FXML ne peut pas être chargé
            System.out.println("Erreur de chargement FXML : " + e.getMessage());
        }
    }
}

