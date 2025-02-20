package gui;

import Entitie.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AjouterUser {



    private final UserService userService = new UserService();

    @FXML
    private TextField TFId;

    @FXML
    private TextField TFNom;

    @FXML
    private TextField TFPrenom;

    @FXML
    private TextField TFIdentifiant;

    @FXML
    private TextField TFRole;

    @FXML
    private TextField TFGmail;

    @FXML
    private TextField TFMotdepasse;

    private String Image = ""; // Store the path of the selected image

    @FXML
    void Ajouter(ActionEvent event) {
        try {
            // Récupération des données
            int Id = Integer.parseInt(TFId.getText().trim());
            String Nom = TFNom.getText().trim();
            String Prenom = TFPrenom.getText().trim();
            String Identifiant = TFIdentifiant.getText().trim();
            String Role = TFRole.getText().trim();
            String Gmail = TFGmail.getText().trim();
            String Motdepasse = TFMotdepasse.getText().trim();

            // Création de l'objet User
            User user = new User(Id, Nom, Prenom, Gmail, Identifiant, Role, Motdepasse, Image);

            // Vérification des champs et unicité de l'identifiant
            if (!userService.validateUser(user)) {

                return;
            }
            if (!userService.isUniqueIdentifiant(user.getIdentifiant(), user.getId())) {

                afficherAlerte("Erreur d'unicité", "Cet identifiant est déjà utilisé !");
                return;
            }

            // Insertion de l'utilisateur
            userService.insert(user);


            // Réinitialiser les champs après insertion
            viderChamps();

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "L'ID doit être un nombre valide.");
        }
    }

    @FXML
    void onImageSelect(ActionEvent event) {
        // Open a file chooser for the user to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            Image = file.getAbsolutePath(); // Set the image path
        }
    }

    @FXML
    private void handleSignIn(ActionEvent event) throws IOException {
        // Charger le fichier FXML de la nouvelle interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
        Parent root = loader.load();

        // Obtenir la scène actuelle et la remplacer par la nouvelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Méthode pour afficher des alertes
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour vider les champs après l'insertion
    private void viderChamps() {
        TFId.clear();
        TFNom.clear();
        TFPrenom.clear();
        TFIdentifiant.clear();
        TFRole.clear();
        TFGmail.clear();
        TFMotdepasse.clear();
    }
}
