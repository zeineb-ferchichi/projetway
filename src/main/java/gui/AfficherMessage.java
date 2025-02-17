package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import models.Message;
import services.MessageService;

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
        // Vérification si messageTilePane est correctement initialisé
        if (messageTilePane == null) {
            System.err.println("Le conteneur des messages est nul.");
            return;
        }

        messageTilePane.getChildren().clear(); // Nettoyer les anciens messages

        try {
            List<Message> messages = messageService.getAllByForumId(forumId);

            // Vérification s'il y a des messages à afficher
            if (messages.isEmpty()) {
                Label noMessagesLabel = new Label("Aucun message dans ce forum.");
                messageTilePane.getChildren().add(noMessagesLabel);
            } else {
                // Affichage des messages
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
            // Affichage d'un message d'erreur si l'appel à la méthode échoue
            e.printStackTrace();
            Label errorLabel = new Label("Erreur lors du chargement des messages.");
            messageTilePane.getChildren().add(errorLabel);
        }
    }

    // Méthode pour modifier un message
    private void modifyMessage(Message message) {
        // Logique de modification ici, peut-être ouvrir une fenêtre ou un formulaire
        System.out.println("Modification du message : " + message.getContenu());
    }

    // Méthode pour supprimer un message
    private void deleteMessage(Message message) {
        // Appel au service pour supprimer le message
        try {
            messageService.delete(message); // Suppression du message via le service

            // Mise à jour de l'affichage après la suppression
            afficherMessages();

            // Optionnel: Affichage d'un message de confirmation
            System.out.println("Message supprimé : " + message.getContenu());

        } catch (Exception e) {
            // Affichage d'un message d'erreur si la suppression échoue
            e.printStackTrace();
            Label errorLabel = new Label("Erreur lors de la suppression du message.");
            messageTilePane.getChildren().add(errorLabel);
        }
    }
}
