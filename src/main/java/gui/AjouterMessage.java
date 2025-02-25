package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import models.Forum;
import models.Message;
import okhttp3.*;
import org.json.JSONObject;
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
    private TilePane tilePane;

    @FXML
    private Button btnAfficherMessages; // Bouton pour afficher les messages

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button btnAfficherForum;

    private final MessageService messageService = new MessageService();
    private final ForumService forumService = new ForumService();
    private int forumId; // Stocke l'ID du forum sélectionné
    private static final String API_KEY = "AIzaSyChr4M6I2yRTS3hArYsm4DjJTEi1wYi2Xc";
    @FXML
    public void initialize() {

        // Action sur le bouton d'envoi
        btnEnvoyer.setOnAction(event -> ajouterMessage());
    }

    // Méthode pour initialiser les données avec le forum sélectionné
    public void initData(Forum forum) {
        this.forumId = forum.getIdForum(); // ✅ Récupérer l'ID du forum
        System.out.println(forum);
        Platform.runLater(() -> {
            afficherForum(forum);
        });

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
       contenu= isContentInappropriate(contenu);
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

    @FXML
    void afficherMessages(ActionEvent event) {
        try {
            // Charger l'interface AfficherMessage.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherMessage.fxml"));

            // Vérifiez si le chemin est correct et que le fichier est accessible
            if (loader.getLocation() == null) {
                System.out.println("Le fichier FXML n'a pas pu être chargé !");
                return;
            }

            Parent root = loader.load();

            // Obtenir le contrôleur de l'interface AfficherMessage
            AfficherMessage controller = loader.getController();

            // Passer l'ID du forum à l'interface AfficherMessage
            System.out.println(this.forumId);
            controller.initData( this.forumId);

            // Changer la scène actuelle pour afficher l'interface de messages
            messageInput.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
        }
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
     tilePane.getChildren().add(forumBox);
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
