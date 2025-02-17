package gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import models.Message;
import services.MessageService;

public class ModifierMessage {

    @FXML
    private Button btnModifier;

    @FXML
    private VBox messageModifierContainer;

    @FXML
    private TextField messageModifierInput;

    @FXML
    private ScrollPane scrollPaneModifier;

    private Message message;
    private final MessageService messageService = new MessageService();

    // Méthode pour initialiser les données du message à modifier
    public void initData(Message message) {
        this.message = message;
        messageModifierInput.setText(message.getContenu());
    }

    // Méthode pour modifier le message

    // Dans la méthode de modification du message (modifierMessage)
    @FXML
    private void modifierMessage() {
        if (message != null) {
            String nouveauContenu = messageModifierInput.getText();
            if (nouveauContenu.isEmpty()) {
                // Alerte si le champ est vide
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Champ vide");
                alert.setHeaderText(null);
                alert.setContentText("Le message ne peut pas être vide.");
                alert.showAndWait();
            } else {
                // Mise à jour du message
                message.setContenu(nouveauContenu);
                messageService.update(message);

                // Alerte de succès
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Modification réussie");
                alert.setHeaderText(null);
                alert.setContentText("Le message a été modifié avec succès.");
                alert.showAndWait();

                // Afficher la scène précédente après modification
                try {
                    // Charger la scène "AfficherMessage.fxml" et récupérer le contrôleur
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherMessage.fxml"));
                    Scene afficherMessageScene = new Scene(loader.load());
                    Stage stage = (Stage) btnModifier.getScene().getWindow();

                    // Passer l'information à la scène précédente pour la rafraîchir
                    AfficherMessage afficherMessageController = loader.getController();
                    afficherMessageController.refreshMessages(message.getIdforum());  // Passer l'ID du forum pour recharger le forum actuel

                    // Changer la scène et l'afficher
                    stage.setScene(afficherMessageScene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alertError = new Alert(AlertType.ERROR);
                    alertError.setTitle("Erreur de chargement");
                    alertError.setContentText("Erreur lors du chargement de la scène précédente.");
                    alertError.showAndWait();
                }
            }
        }
    }

}
