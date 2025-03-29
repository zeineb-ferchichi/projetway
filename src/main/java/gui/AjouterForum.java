package gui;

import models.Forum;
import org.json.JSONObject;
import services.ForumService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;


public class AjouterForum {

    private final ForumService forumService = new ForumService();  // Création de l'objet ForumService
    private static List<Forum> forumsList = new ArrayList<>(); // Liste statique pour stocker les forums ajoutés
    private static final String API_KEY = "AIzaSyChr4M6I2yRTS3hArYsm4DjJTEi1wYi2Xc";
    @FXML
    private TextField TFcontenue;

    @FXML
    private ImageView TFimage;

    @FXML
    private TextField TFtitre;

    @FXML
    private Button btnAfficherForum; // Bouton pour afficher les forums
    private String imagePath ="";
    @FXML
    void ajouter(ActionEvent event) {
        String titre = TFtitre.getText().trim();
        String contenu = TFcontenue.getText().trim();

        // Vérification des champs vides
        if (titre.isEmpty() || contenu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérification de la longueur du titre
        if (titre.length() < 3 || titre.length() > 50) {
            showAlert(Alert.AlertType.WARNING, "Titre invalide", "Le titre doit contenir entre 3 et 50 caractères.");
            return;
        }

        // Vérification de la longueur du contenu
        if (contenu.length() < 10 || contenu.length() > 500) {
            showAlert(Alert.AlertType.WARNING, "Contenu invalide", "Le contenu doit contenir entre 10 et 500 caractères.");
            return;
        }

        // Vérification de l'image
        if (TFimage.getImage() == null) {
            showAlert(Alert.AlertType.WARNING, "Image manquante", "Veuillez sélectionner une image.");
            return;
        }

        String sanitizedTitre = isContentInappropriate(titre);
        String sanitizedContenu = isContentInappropriate(contenu);
        if (sanitizedTitre.contains("*") || sanitizedContenu.contains("*")) {
            showAlert(Alert.AlertType.ERROR, "Contenu interdit", "Votre message contient des propos inappropriés.");
        }

        TFtitre.setText(sanitizedTitre);
        TFcontenue.setText(sanitizedContenu);

        // **UPLOAD IMAGE FIRST** (if it's not already a URL)
        if (imagePath != null && !imagePath.startsWith("http")) {
            try {
                imagePath = uploadImageToServer(new File(imagePath));  // Upload & get URL
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du téléversement de l'image.");
                return;
            }
        }

        // Création d'un nouveau Forum avec l'URL de l'image
        try {
            Forum forum = new Forum(sanitizedTitre, sanitizedContenu, imagePath, new Date(System.currentTimeMillis()));

            forumService.add(forum); // Enregistrement dans la base de données

            // Ajouter le forum à la liste statique
            forumsList.add(forum);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Forum ajouté avec succès !");

            // Réinitialiser les champs après l'ajout
            TFtitre.clear();
            TFcontenue.clear();
            TFimage.setImage(null);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du forum : " + e.getMessage());
        }
    }


    /**
     * Méthode pour afficher des alertes
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     *
     * Sélectionner une image à l'aide de FileChooser
     */
    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.toURI().toString();

            TFimage.setImage(new Image(imagePath));  // Afficher l'image sélectionnée dans l'ImageView
            imagePath=  file.toPath().toString();
            System.out.println(imagePath);
        }
    }

    // Méthode pour obtenir la liste des forums
    public static List<Forum> getForumsList() {
        return forumsList; // Retourne la liste statique des forums
    }

    /**
     * Méthode pour afficher l'écran des forums
     */
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
    public String uploadImageToServer(File imageFile) throws IOException {
        String uploadUrl = "http://localhost:8000/upload-image";
        HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();
        System.out.println("here");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----Boundary");
        System.out.println("here é");

        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

        // Send file data
        writer.append("------Boundary\r\n")
                .append("Content-Disposition: form-data; name=\"image\"; filename=\"" + imageFile.getName() + "\"\r\n")
                .append("Content-Type: " + Files.probeContentType(imageFile.toPath()) + "\r\n\r\n")
                .flush();
        System.out.println("here 22");

        Files.copy(imageFile.toPath(), outputStream);
        outputStream.flush();
        writer.append("\r\n------Boundary--\r\n").flush();

        // Get response from Symfony
        InputStream responseStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        // Parse JSON response to extract the URL
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getString("url");
    }
}
