package gui;

import Entitie.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Service.UserService;

import java.io.IOException;

import static Entitie.User.currentUser;

public class signIn {

    @FXML
    private TextField identifiantField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Hyperlink createAccountLink;

    private final UserService userService = new UserService();


    @FXML
    private void handleCreateAccount(ActionEvent event) {
        try {
            // Charger le fichier FXML de Create Account
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Create Account");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle (optionnel)
            ((Stage) ((Hyperlink) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        String identifiant = identifiantField.getText();
        String password = passwordField.getText();

        if (identifiant.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Veuillez entrer l'identifiant et le mot de passe.");
            return;
        }

        User user = userService.getUserByIdentifiant(identifiant);

        if (user != null && user.getMotdepasse().equals(password)) {
            // Vérifier le rôle et ouvrir l'interface correspondante
            ouvrirInterfaceSelonRole(event, user);
        } else {
            showAlert(Alert.AlertType.ERROR, "Échec de connexion", "Identifiant ou mot de passe invalide.");
        }
    }





    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void ouvrirInterfaceSelonRole(ActionEvent event, User user) {
        try {
            if (user == null) {
                System.out.println("Erreur: user est null après connexion !");
                return;
            }

            System.out.println("Utilisateur connecté: " + user.getNom()
                    + " ID: " + user.getId()
                    + " Rôle: " + user.getRole());

            FXMLLoader loader;
            Parent root;

            // Vérification du rôle
            switch (user.getRole().toLowerCase()) {
                case "admin":
                    loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
                    root = loader.load();
                    AdminController adminController = loader.getController();
                    adminController.setCurrentUser(user);
                    break;

                case "employe":
                    loader = new FXMLLoader(getClass().getResource("/employe.fxml"));
                    root = loader.load();
                    EmployeController employeController = loader.getController();
                    employeController.setCurrentUser(user);
                    break;

               /* case "directeur":
                    loader = new FXMLLoader(getClass().getResource("/directeur.fxml"));
                    root = loader.load();
                    DirecteurController directeurController = loader.getController();
                    directeurController.setCurrentUser(user);
                    break;*/

                default:
                    showAlert(Alert.AlertType.ERROR, "Accès refusé", "Votre rôle ne permet pas d'accéder à cette application.");
                    return;
            }

            // Affichage de la nouvelle scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface.");
        }
    }






}

