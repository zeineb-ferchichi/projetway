package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Forum;
import models.Message;
import services.ForumService;
import services.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AfficherMessage {

    @FXML
    private HBox buttonBox;

    @FXML
    private Button deleteButton;

    @FXML
    private VBox messageCard;

    @FXML
    private Label messageLabel;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private TilePane messageTilePane;

    @FXML
    private Button modifyButton;
    @FXML
    private Button btnAfficherForum;
    @FXML
    private TilePane tilePane;
    @FXML
    private Button btnRetour;
    private Forum forum;
    private final ForumService forumService = new ForumService();
    private final MessageService messageService = new MessageService();
    private int forumId; // Forum sélectionné

    // Méthode pour initialiser les données avec l'ID du forum et afficher les messages
    public void initData(int forumId) {
        this.forumId = forumId;
        forum = forumService.getById(forumId);
        btnRetour.setOnAction(e -> backButton(forum));
        afficherForum(forum);
        afficherMessages(); // Charge et affiche les messages du forum
    }

    // Affiche tous les messages du forum sélectionné
    private void afficherMessages() {
        if (messageTilePane == null) {
            System.err.println("Le conteneur des messages est nul.");
            return;
        }

        messageTilePane.getChildren().clear(); // Nettoyer les anciens messages

        try {
            List<Message> messages = messageService.getAllByForumId(forumId);

            if (messages.isEmpty()) {
                Label noMessagesLabel = new Label("Aucun message dans ce forum.");
                messageTilePane.getChildren().add(noMessagesLabel);
            } else {
                for (Message msg : messages) {
                    VBox messageBox = new VBox();
                    messageBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10px; -fx-border-radius: 5px; -fx-margin-bottom: 5px;");

                    Label contenuLabel = new Label(msg.getContenu());
                    contenuLabel.setWrapText(true);

                    Label dateLabel = new Label("Envoyé le : " + msg.getDateEnvoi().toString());
                    dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

                    Button modifyButton = new Button("Modifier");
                    modifyButton.setStyle("-fx-background-color: #62B9CB; -fx-text-fill: white; -fx-background-radius: 5;");
                    modifyButton.setOnAction(event -> modifyMessage(msg));

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setStyle("-fx-background-color: #D9534F; -fx-text-fill: white; -fx-background-radius: 5;");
                    deleteButton.setOnAction(event -> deleteMessage(msg));

                    HBox buttonBox = new HBox(10, modifyButton, deleteButton);

                    messageBox.getChildren().addAll(contenuLabel, dateLabel, buttonBox);
                    messageTilePane.getChildren().add(messageBox);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Erreur lors du chargement des messages.");
            messageTilePane.getChildren().add(errorLabel);
        }
    }

    // Méthode pour modifier un message
    private void modifyMessage(Message message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierMessage.fxml"));
            Parent root = loader.load();

            ModifierMessage controller = loader.getController();
            if (controller != null) {
                controller.initData(message,forumId);
            } else {
                System.err.println("Erreur: Impossible de récupérer le contrôleur ModifierMessage.");
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Message");
            stage.show();
            stage.setOnHiding(event -> {
                afficherMessages();

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un message
    private void deleteMessage(Message message) {
        try {
            messageService.delete(message);
            afficherMessages();
            System.out.println("Message supprimé : " + message.getContenu());
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Erreur lors de la suppression du message.");
            messageTilePane.getChildren().add(errorLabel);
        }
    }

    public void refreshMessages() {
        afficherMessages(); // Appeler la méthode pour recharger et afficher les messages après modification
    }

    // Méthode pour rafraîchir les messages du forum spécifique
    public void refreshMessages(int forumId) {
        this.forumId = forumId;  // Utilisation de forumId au lieu de currentForumId
        afficherMessages();  // Appeler la méthode pour recharger et afficher les messages après modification
    }
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
    void afficherForum(Forum forum){
        if(tilePane == null){
            System.out.println("tilePane is NULL! Cannot display forum.");
            return;
        }
        tilePane.getChildren().clear();
        VBox forumBox = new VBox();
        forumBox.setSpacing(10);
        forumBox.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");
        String imagePath = forum.getImage();
        File imageFile = new File(imagePath);
        Image image;
        if (imageFile.exists() && imageFile.isFile()) {
            image = new Image(imagePath);
        } else {
            image = new Image(imagePath);
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
        tilePane.getChildren().add(forumBox);
    }
    private void backButton(Forum forum) {
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