package gui;

import models.Forum;
import services.ForumService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.TextField;

public class AfficherForum {

    private final ForumService forumService = new ForumService(); // Service pour interagir avec la BDD

    @FXML
    private Button btnModifier, btnSupprimer;

    @FXML
    private Label forumContent, forumTitle;

    @FXML
    private ImageView forumImageView;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private TextField TFtitre; // Vérification si ce champ existe bien dans ModifierForum.fxml

    @FXML
    void initialize() {
        refreshForums();
    }

    private void refreshForums() {
        tilePane.getChildren().clear();

        try {
            List<Forum> forums = forumService.getAll();

            for (Forum forum : forums) {
                VBox forumBox = new VBox();
                forumBox.setSpacing(10);
                forumBox.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");

                String imagePath = forum.getImage();
                File imageFile = new File(imagePath);
                Image image;

                if (imageFile.exists() && imageFile.isFile()) {
                    image = new Image("file:" + imagePath);
                } else {
                    image = new Image("file:/chemin/vers/image/empty.png");
                }

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                forumBox.getChildren().add(imageView);

                Label titleLabel = new Label(forum.getTitre());
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                forumBox.getChildren().add(titleLabel);

                Label contentLabel = new Label(forum.getContenu());
                contentLabel.setWrapText(true);
                forumBox.getChildren().add(contentLabel);

                Button btnModifier = new Button("Modifier");
                btnModifier.setStyle("-fx-background-color: #62B9CB; -fx-text-fill: white;");
                btnModifier.setOnAction(e -> modifierForum(forum));

                Button btnSupprimer = new Button("Supprimer");
                btnSupprimer.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white;");
                btnSupprimer.setOnAction(e -> supprimerForum(forum));

                Button btnMessage = new Button("Message");
                btnMessage.setStyle("-fx-background-color:#1B4B65; -fx-text-fill: white;");
                btnMessage.setOnAction(e -> ouvrirAjouterMessage(forum));

                HBox buttonContainer = new HBox(10, btnModifier, btnSupprimer, btnMessage);
                forumBox.getChildren().add(buttonContainer);

                tilePane.getChildren().add(forumBox);
            }
        } catch (Exception e) {
            showError("Erreur lors du chargement des forums", e);
        }
    }

    @FXML
    private void modifierForum(Forum forum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierForum.fxml"));
            Parent root = loader.load();

            ModifierForum controller = loader.getController();
            controller.setForum(forum);

            Stage stage = (Stage) tilePane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de ModifierForum.fxml", e);
        }
    }

    private void supprimerForum(Forum forum) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce forum ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                forumService.delete(forum);
                refreshForums();
            }
        });
    }

    private void ouvrirAjouterMessage(Forum forum) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterMessage.fxml"));
            Parent root = loader.load();

            AjouterMessage controller = loader.getController();
            controller.initData(forum);

            Stage stage = (Stage) tilePane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de AjouterMessage.fxml", e);
        }
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
}
