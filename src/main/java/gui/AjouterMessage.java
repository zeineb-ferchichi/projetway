package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.io.IOException;
import models.Forum;
import models.Message;
import services.MessageService;
import services.ForumService;

import java.sql.Date;
import java.util.List;

public class AjouterMessage {

    @FXML
    private VBox messageContainer;

    @FXML
    private TextField messageInput;

    @FXML
    private Button btnEnvoyer;

    @FXML
    private Button btnAfficherMessages; // Bouton pour afficher les messages

    @FXML
    private ScrollPane scrollPane;

    private final MessageService messageService = new MessageService();
    private final ForumService forumService = new ForumService();
    private int forumId; // Stocke l'ID du forum sélectionné

    @FXML
    public void initialize() {
        // Action sur le bouton d'envoi
        btnEnvoyer.setOnAction(event -> ajouterMessage());
    }

    // Méthode pour initialiser les données avec le forum sélectionné
    public void initData(Forum forum) {
        this.forumId = forum.getIdForum(); // ✅ Récupérer l'ID du forum
        afficherMessages(); // Affiche les messages du forum au démarrage
    }

    // Affiche tous les messages du forum sélectionné
    private void afficherMessages() {
        messageContainer.getChildren().clear(); // Nettoyer les anciens messages avant de les recharger
        try {
            List<Message> messages = messageService.getAllByForumId(forumId);

            // Vérification s'il y a des messages à afficher
            if (messages.isEmpty()) {
                Label noMessagesLabel = new Label("Aucun message dans ce forum.");
                messageContainer.getChildren().add(noMessagesLabel);
            } else {
                // Affichage des messages
                for (Message msg : messages) {
                    VBox messageBox = new VBox();
                    messageBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10px; -fx-border-radius: 5px; -fx-margin-bottom: 5px;");

                    Label contenuLabel = new Label(msg.getContenu());
                    contenuLabel.setWrapText(true);

                    Label dateLabel = new Label("Envoyé le : " + msg.getDateEnvoi().toString());
                    dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");

                    messageBox.getChildren().addAll(contenuLabel, dateLabel);
                    messageContainer.getChildren().add(messageBox);
                }
            }

            // Faire défiler vers le bas après le chargement des messages
            scrollPane.setVvalue(1.0);  // Fait défiler le ScrollPane vers le bas

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des messages.");
        }
    }

    // Ajoute un nouveau message et met à jour l'affichage
    private void ajouterMessage() {
        String contenu = messageInput.getText().trim();

        // Vérification si le contenu du message est non vide
        if (!contenu.isEmpty()) {
            // Vérification si le forum existe avant d'ajouter le message
            if (forumId > 0 && forumService.getById(forumId) != null) {
                try {
                    // Création du message
                    Message message = new Message(contenu, new Date(System.currentTimeMillis()), forumId);

                    // Ajouter le message à la base de données
                    messageService.add(message);

                    // Effacer la zone de texte
                    messageInput.clear();

                    // Rafraîchir l'affichage des messages
                    afficherMessages();  // Cette ligne met à jour les messages affichés

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors de l'ajout du message.");
                    // Affichage de l'alerte d'erreur lors de l'ajout
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Erreur lors de l'ajout du message");
                    alert.setContentText("Une erreur est survenue lors de l'ajout du message.");
                    alert.showAndWait();
                }
            } else {
                // Affichage d'un message d'erreur si le forum n'existe pas
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Forum non trouvé");
                alert.setHeaderText("Forum non trouvé");
                alert.setContentText("Le forum avec cet ID n'existe pas.");
                alert.showAndWait();
            }
        } else {
            // Message d'erreur si le contenu est vide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message vide");
            alert.setHeaderText("Le message est vide");
            alert.setContentText("Le message ne peut pas être vide.");
            alert.showAndWait();
        }
    }

    // Méthode pour afficher l'interface "AfficherMessage"
    @FXML
    void afficherMessages(ActionEvent event) {
        try {
            // Charger l'interface AfficherMessage.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherMessage.fxml"));

            // Changer la scène actuelle pour afficher l'interface de messages
            messageInput.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
