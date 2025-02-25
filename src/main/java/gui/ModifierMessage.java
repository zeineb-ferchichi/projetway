package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import models.Message;
import org.json.JSONObject;
import services.MessageService;
import java.io.IOException;
import okhttp3.*;
public class ModifierMessage {
    private static final String API_KEY = "AIzaSyChr4M6I2yRTS3hArYsm4DjJTEi1wYi2Xc";
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
    public void initData(Message message, int forumid) {
        if (message == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Message non trouvé", "Impossible de charger le message.");
            return;
        }
        this.message = message;
        if (messageModifierInput != null) {
            messageModifierInput.setText(message.getContenu());
        }
    }

    // Méthode pour modifier le message
    @FXML
    private void modifierMessage() {
        if (message == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun message sélectionné", "Veuillez sélectionner un message valide.");
            return;
        }

        String nouveauContenu = messageModifierInput.getText().trim();
        if (nouveauContenu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Le message ne peut pas être vide", "Veuillez entrer un texte valide.");
            return;
        }
        nouveauContenu =  isContentInappropriate(nouveauContenu);
        try {
            // Mise à jour du message
            message.setContenu(nouveauContenu);
            messageService.update(message);

            showAlert(Alert.AlertType.INFORMATION, "Modification réussie", null, "Le message a été modifié avec succès.");

            // Fermer la fenêtre actuelle après modification
            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.close();

            // Recharger la liste des messages
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue", "Impossible de modifier le message.");
        }
    }

    // Retourner à la liste des messages
    private void retournerAListeMessages() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherMessage.fxml"));
            Parent root = loader.load();

            // Passer l'information à la scène précédente pour la rafraîchir
            AfficherMessage afficherMessageController = loader.getController();
            if (afficherMessageController != null) {
                afficherMessageController.refreshMessages(message.getIdforum());
            }

            // Changer la racine de la scène et l'afficher
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", null, "Erreur lors du chargement de la scène précédente.");
        }
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private String isContentInappropriate(String text) {
        OkHttpClient client = new OkHttpClient();
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

        JSONObject requestBody = new JSONObject();
        JSONObject content = new JSONObject();
        String prompt = "Analyze the following text and replace any hate speech, profanity, or offensive words with '*****'. "
                + "Ensure that only harmful words are censored while keeping the sentence structure intact. "
                + "Return only the modified text.\n\n"
                + "Text: " + text;
        content.put("text", prompt);

        JSONObject contents = new JSONObject();
        contents.put("parts", new org.json.JSONArray().put(content));

        requestBody.put("contents", new org.json.JSONArray().put(contents));

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject jsonResponse = new JSONObject(response.body().string());
            String sanitizedText = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .trim();

            return sanitizedText;
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de vérification AI : " + e.getMessage());
            return text;
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}