package gui;

import models.Forum;
import services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AjouterForum {

    private final ForumService forumService = new ForumService();  // Création de l'objet ForumService
    private static List<Forum> forumsList = new ArrayList<>(); // Liste statique pour stocker les forums ajoutés

    @FXML
    private TextField TFcontenue;

    @FXML
    private ImageView TFimage;

    @FXML
    private TextField TFtitre;

    @FXML
    private Button btnAfficherForum; // Bouton pour afficher les forums
private String imagePath ="";
    @FXML
    void ajouter(ActionEvent event) {
        String titre = TFtitre.getText().trim();
        String contenu = TFcontenue.getText().trim();

        // Vérification des champs vides
        if (titre.isEmpty() || contenu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérification de la longueur du titre
        if (titre.length() < 3 || titre.length() > 50) {
            showAlert(Alert.AlertType.WARNING, "Titre invalide", "Le titre doit contenir entre 3 et 50 caractères.");
            return;
        }

        // Vérification de la longueur du contenu
        if (contenu.length() < 10 || contenu.length() > 500) {
            showAlert(Alert.AlertType.WARNING, "Contenu invalide", "Le contenu doit contenir entre 10 et 500 caractères.");
            return;
        }

        // Vérification de l'image
        if (TFimage.getImage() == null) {
            showAlert(Alert.AlertType.WARNING, "Image manquante", "Veuillez sélectionner une image.");
            return;
        }

        // Récupérer l'image depuis ImageView

        try {
            Forum forum = new Forum(titre, contenu, imagePath, new Date(System.currentTimeMillis()));

            forumService.add(forum); // Utilisation de ForumService pour l'ajout

            // Ajouter le forum à la liste statique
            forumsList.add(forum);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Forum ajouté avec succès !");

            // Réinitialiser les champs après l'ajout
            TFtitre.clear();
            TFcontenue.clear();
            TFimage.setImage(null);

        } catch (Exception e) {  // Exception générale au lieu de IOException
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du forum : " + e.getMessage());
        }
    }

    /**
     * Méthode pour afficher des alertes
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sélectionner une image à l'aide de FileChooser
     */
    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
             imagePath = file.toURI().toString();

            TFimage.setImage(new Image(imagePath));  // Afficher l'image sélectionnée dans l'ImageView
            imagePath=  file.toPath().toString();
            System.out.println(imagePath);
        }
    }

    // Méthode pour obtenir la liste des forums
    public static List<Forum> getForumsList() {
        return forumsList; // Retourne la liste statique des forums
    }

    /**
     * Méthode pour afficher l'écran des forums
     */
    @FXML
    void afficher(ActionEvent event) {
        try {
            // Charge le fichier FXML de l'écran des forums
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherForum.fxml"));
            // Change la scène pour afficher la nouvelle interface
            btnAfficherForum.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
