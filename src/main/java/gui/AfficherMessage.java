/*package gui;

import models.Message;
import services.MessageService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import java.util.List;
import java.util.Optional;

public class AfficherMessage {

    private final MessageService messageService = new MessageService(); // Service pour interagir avec la BDD
    private int forumId; // ID du forum à afficher

    @FXML
    private TilePane messageTilePane; // Conteneur pour les cartes de message

    @FXML
    private ScrollPane messageScrollPane; // ScrollPane pour l'affichage des messages

    @FXML
    void initialize() {
        forumId = 1; // Récupère dynamiquement l'ID du forum si possible
        messageTilePane.setPrefColumns(1); // Afficher un seul message par ligne
        refreshMessages();
    }

    // Méthode pour récupérer et afficher les messages dans le TilePane
    private void refreshMessages() {
        messageTilePane.getChildren().clear(); // Nettoie l'affichage avant de recharger les messages

        try {
            List<Message> messages = messageService.getAllByForumId(forumId);

            if (messages.isEmpty()) {
                Label noMessageLabel = new Label("Aucun message disponible.");
                messageTilePane.getChildren().add(noMessageLabel);
                return;
            }

            for (Message message : messages) {
                // Création de la carte de message
                VBox messageBox = new VBox(10);
                messageBox.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-background-radius: 10; "
                        + "-fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");
                messageBox.setAlignment(Pos.CENTER_LEFT);

                // Contenu du message
                Label contentLabel = new Label(message.getContenu());
                contentLabel.setWrapText(true);

                // Création des boutons Modifier et Supprimer
                Button btnModify = new Button("Modifier");
                Button btnDelete = new Button("Supprimer");

                // Actions des boutons
                btnModify.setOnAction(e -> modifierMessage(message));
                btnDelete.setOnAction(e -> supprimerMessage(message));

                // Ajouter les éléments à la VBox
                HBox buttonBox = new HBox(10, btnModify, btnDelete);
                buttonBox.setAlignment(Pos.CENTER);

                messageBox.getChildren().addAll(contentLabel, buttonBox);

                // Ajouter la carte de message au TilePane
                messageTilePane.getChildren().add(messageBox);
            }
        } catch (Exception e) {
            afficherAlerte(AlertType.ERROR, "Erreur", "Erreur lors du chargement des messages", e.getMessage());
        }
    }

    // Méthode pour modifier un message
    private void modifierMessage(Message message) {
        // Création de la boîte de dialogue personnalisée
        TextInputDialog dialog = new TextInputDialog(message.getContenu());
        dialog.setTitle("Modifier le message");
        dialog.setHeaderText("Modifiez le contenu du message :");
        dialog.setContentText("Nouveau contenu :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nouveauContenu -> {
            if (!nouveauContenu.trim().isEmpty()) {
                message.setContenu(nouveauContenu.trim());
                messageService.update(message);
                refreshMessages();
            } else {
                afficherAlerte(AlertType.WARNING, "Erreur", "Contenu vide", "Le contenu du message ne peut pas être vide.");
            }
        });
    }

    // Méthode pour supprimer un message
    private void supprimerMessage(Message message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce message ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            messageService.delete(message);
            refreshMessages();
        }
    }

    // Méthode utilitaire pour afficher une alerte
    private void afficherAlerte(AlertType type, String titre, String enTete, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(enTete);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
*/