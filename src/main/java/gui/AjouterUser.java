package gui;

import Entitie.User;
import Service.UserService;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AjouterUser {

    private final UserService userService = new UserService();

    @FXML
    private TextField TFNom;

    @FXML
    private TextField TFPrenom;

    @FXML
    private TextField TFGmail;

    @FXML
    private PasswordField TFMotdepasse;
    @FXML
    private TextField TFMotdepasseVisible;

    @FXML
    private PasswordField TFConfirmMotdepasse;

    // Bouton avec image pour basculer la visibilité du mot de passe
    @FXML
    private Button btnTogglePassword;

    private boolean passwordVisible = false;


    private String ImagePath = ""; // Chemin de l'image sélectionnée

    // Rôle par défaut
    private final String defaultRole = "employe";

    @FXML
    public void initialize() {


    }



    @FXML
    void togglePasswordVisibility(ActionEvent event) {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            TFMotdepasseVisible.setText(TFMotdepasse.getText());
            TFMotdepasseVisible.setVisible(true);
            TFMotdepasse.setVisible(false);
            btnTogglePassword.setText("👁");
        } else {
            TFMotdepasse.setText(TFMotdepasseVisible.getText());
            TFMotdepasseVisible.setVisible(false);
            TFMotdepasse.setVisible(true);
            btnTogglePassword.setText("🙈");
        }
    }


    @FXML
    void Ajouter(ActionEvent event) {
        String nom = TFNom.getText().trim();
        String prenom = TFPrenom.getText().trim();
        String gmail = TFGmail.getText().trim();
        String motdepasse = TFMotdepasse.getText().trim();
        String confirmMotdepasse = TFConfirmMotdepasse.getText().trim();

        if (!motdepasse.equals(confirmMotdepasse)) {
            afficherAlerte("Erreur", "Les mots de passe ne correspondent pas !");
            return;
        }

        // Génération d'un identifiant aléatoire
        String generatedIdentifiant = generateRandomIdentifiant();

        // Cryptage du mot de passe avec BCrypt
        String hashedPassword = BCrypt.withDefaults().hashToString(12, motdepasse.toCharArray());

        // Création de l'objet User
        User user = new User(nom, prenom, gmail, generatedIdentifiant, defaultRole, hashedPassword, ImagePath);

        if (!userService.validateUser(user)) {
            return;
        }
        if (!userService.isUniqueIdentifiant(user.getIdentifiant(), user.getId())) {
            afficherAlerte("Erreur d'unicité", "L'identifiant généré est déjà utilisé, veuillez réessayer !");
            return;
        }

        userService.insert(user);
        viderChamps();
    }

    @FXML
    void onImageSelect(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            ImagePath = file.getAbsolutePath();
        }
    }

    @FXML
    private void handleSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signIn.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        TFNom.clear();
        TFPrenom.clear();
        TFGmail.clear();
        TFMotdepasse.clear();
        TFConfirmMotdepasse.clear();
    }

    // Méthode de génération d'un identifiant au format "221JMT5109"
    private String generateRandomIdentifiant() {
        String part1 = generateRandomDigits(3);
        String part2 = generateRandomLetters(3);
        String part3 = generateRandomDigits(4);
        return part1 + part2 + part3;
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateRandomLetters(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char letter = (char) (random.nextInt(26) + 'A');
            sb.append(letter);
        }
        return sb.toString();
    }
}
