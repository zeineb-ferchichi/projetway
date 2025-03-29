package gui;

import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.TextField;

public class AfficherForum {

    private final ForumService forumService = new ForumService();

    @FXML
    private Button btnModifier, btnSupprimer;

    @FXML
    private Label forumContent, forumTitle;

    @FXML
    private ImageView forumImageView;

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private Button searchButton;
    @FXML
    private ImageView sideImageView;
    private List<VBox> allForums = new ArrayList<>();

    @FXML
    void initialize() {
        Image image = new Image(getClass().getResource("/images/main.jpg").toExternalForm());
        sideImageView.setImage(image);
        refreshForums();
        searchButton.setOnAction(event -> filterForums());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterForums());
    }

    private void filterForums() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            tilePane.getChildren().setAll(allForums);
            return;
        }


        List<VBox> filteredForums = allForums.stream()
                .filter(forumBox -> {
                    Label titleLabel = (Label) forumBox.lookup("#forumTitle");
                    return titleLabel.getText().toLowerCase().contains(searchText);
                })
                .collect(Collectors.toList());

        tilePane.getChildren().setAll(filteredForums);
    }

    private void refreshForums() {
        tilePane.getChildren().clear();
        allForums.clear();

        Button btnAjouterInterface = new Button("Ajouter Forum");
        btnAjouterInterface.setStyle("-fx-background-color:#1B4B65; -fx-text-fill: white;");
        btnAjouterInterface.setOnAction(e -> ajouter(btnAjouterInterface));
        tilePane.getChildren().add(btnAjouterInterface);

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
                    image = new Image(imagePath);
                }

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                forumBox.getChildren().add(imageView);

                Label titleLabel = new Label(forum.getTitre());
                titleLabel.setId("forumTitle");
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
                allForums.add(forumBox);
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

    @FXML
    void ajouter(Button btnAjouterInterface) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ajouterforum.fxml"));
            btnAjouterInterface.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
