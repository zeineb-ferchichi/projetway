package gui;

import models.Forum;
import services.ForumService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;

import java.io.File;
import java.util.List;

public class AfficherForum {

    private final ForumService forumService = new ForumService(); // Service pour interagir avec la BDD
    public Button btnCommenter;

    @FXML
    private Button btnModifier;  // Bouton Modifier pour un forum

    @FXML
    private Button btnSupprimer;  // Bouton Supprimer pour un forum

    @FXML
    private Label forumContent;  // Affichage du contenu du forum

    @FXML
    private ImageView forumImageView;  // Correspond à fx:id dans le FXML

    @FXML
    private Label forumTitle;  // Affichage du titre du forum

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    void initialize() {
        refreshForums(); // Charge les forums au démarrage
    }

    private void refreshForums() {
        tilePane.getChildren().clear(); // Nettoie l'affichage avant de recharger les forums

        try {
            List<Forum> forums = forumService.getAll(); // Assurez-vous que votre ForumService retourne une List

            for (Forum forum : forums) {
                VBox forumBox = new VBox();
                forumBox.setSpacing(10);
                forumBox.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");

                String imagePath = forum.getImage(); // Récupère le chemin de l'image
                System.out.println("Chemin de l'image : " + imagePath); // Debug

                File imageFile = new File(imagePath);
                Image image = null;

                // Vérification de l'existence du fichier image
                if (imageFile.exists() && imageFile.isFile()) {
                    image = new Image("file:" + imagePath);
                    System.out.println("Image trouvée et chargée");
                } else {
                    image = new Image("file:/chemin/vers/image/empty.png"); // Remplacez avec un fichier d'image par défaut
                    System.out.println("Image non trouvée, image par défaut utilisée");
                }

                // Création de l'ImageView
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                // Ajout de l'image à la VBox
                forumBox.getChildren().addAll(imageView);

                // Ajout du titre et du contenu
                Label titleLabel = new Label(forum.getTitre());
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                forumBox.getChildren().add(titleLabel);

                Label contentLabel = new Label(forum.getContenu());
                contentLabel.setWrapText(true);
                forumBox.getChildren().add(contentLabel);

                // Boutons Modifier et Supprimer
                Button btnModifier = new Button("Modifier");
                btnModifier.setStyle("-fx-background-color: #62B9CB; -fx-text-fill: white;");
                btnModifier.setOnAction(e -> modifierForum(forum)); // Action pour modifier un forum

                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white;");
                btnSupprimer.setOnAction(e -> supprimerForum(forum)); // Action pour supprimer un forum
                Button btnSupprimercmp = new Button("message");

                btnSupprimercmp.setStyle("-fx-background-color:#1B4B65; -fx-text-fill: white;");
                // Conteneur pour les boutons
                HBox buttonContainer = new HBox(10, btnModifier, btnSupprimer,btnSupprimercmp);
                forumBox.getChildren().add(buttonContainer);

                // Ajouter la VBox au TilePane
                tilePane.getChildren().add(forumBox);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du chargement des forums : " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Méthode pour modifier un forum
    private void modifierForum(Forum forum) {
        // Implémentation de la logique pour modifier un forum
        // Par exemple, vous pouvez ouvrir un formulaire d'édition ici.
        String nouveauTitre = "Nouveau titre"; // À remplacer par un champ d'édition
        forum.setTitre(nouveauTitre);

        // Mettre à jour la base de données avec la nouvelle valeur
        forumService.update(forum); // Assurez-vous que la méthode update existe dans ForumService

        // Rafraîchissement de la vue
        refreshForums();
    }

    // Méthode pour supprimer un forum
    private void supprimerForum(Forum forum) {
        // Implémentation de la logique pour supprimer un forum
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce forum ?");

        alert.showAndWait().ifPresent(response -> {
            if (response.getText().equals("OK")) {
                forumService.delete(forum); // Assurez-vous que la méthode delete existe dans ForumService
                refreshForums(); // Rafraîchit la liste des forums après la suppression
            }
        });
    }
}
