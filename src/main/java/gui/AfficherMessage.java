package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Message;
import services.MessageService;

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

    private final MessageService messageService = new MessageService();
    private int forumId; // Forum sélectionné

    // Méthode pour initialiser les données avec l'ID du forum et afficher les messages
    public void initData(int forumId) {
        this.forumId = forumId;
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
                    modifyButton.setOnAction(event -> modifyMessage(msg));

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setOnAction(event -> deleteMessage(msg));

                    messageBox.getChildren().addAll(contenuLabel, dateLabel, modifyButton, deleteButton);
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
                controller.initData(message);
            } else {
                System.err.println("Erreur: Impossible de récupérer le contrôleur ModifierMessage.");
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Message");
            stage.show();
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

}
