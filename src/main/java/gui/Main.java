package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main {

    // Méthode appelée par le bouton "➕ Ajouter un Transport"
    @FXML
    private void AjouterTransport(ActionEvent event) {
        System.out.println("Bouton ➕ Ajouter un Transport cliqué !");
        // Ex: Charger AjouterTransport.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/AjouterTransport.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "📋 Afficher les Transports"
    @FXML
    private void AfficherTransport(ActionEvent event) {
        System.out.println("Bouton 📋 Afficher les Transports cliqué !");
        // Ex: Charger AfficherTransport.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/AfficherTransport.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "✏ Modifier un Transport"
    @FXML
    private void ModifierTransport(ActionEvent event) {
        System.out.println("Bouton ✏ Modifier un Transport cliqué !");
        // Ex: Charger ModifierTransport.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/ModifierTransport.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "🗑 Supprimer un Transport"
    @FXML
    private void SupprimerTransport(ActionEvent event) {
        System.out.println("Bouton 🗑 Supprimer un Transport cliqué !");
        // Ex: Charger SupprimerTransport.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/SupprimerTransport.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "➕ Ajouter un Abonnement"
    @FXML
    private void AjouterAbonnement(ActionEvent event) {
        System.out.println("Bouton ➕ Ajouter un Abonnement cliqué !");
        // Ex: Charger AjouterAbonnement.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/AjouterAbonnement.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "📋 Afficher les Abonnements"
    @FXML
    private void AfficherAbonnement(ActionEvent event) {
        System.out.println("Bouton 📋 Afficher les Abonnements cliqué !");
        // Ex: Charger AfficherAbonnement.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/AfficherAbonnement.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "✏ Modifier un Abonnement"
    @FXML
    private void ModifierAbonnement(ActionEvent event) {
        System.out.println("Bouton ✏ Modifier un Abonnement cliqué !");
        // Ex: Charger ModifierAbonnement.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/ModifierAbonnement.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "🗑 Supprimer un Abonnement"
    @FXML
    private void SupprimerAbonnement(ActionEvent event) {
        System.out.println("Bouton 🗑 Supprimer un Abonnement cliqué !");
        // Ex: Charger SupprimerAbonnement.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/SupprimerAbonnement.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée par le bouton "❌ Quitter"
    @FXML
    private void quitter(ActionEvent event) {
        System.out.println("Bouton ❌ Quitter cliqué !");
        // Fermer la fenêtre
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}