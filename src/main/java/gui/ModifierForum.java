package gui;

import models.Forum;
import okhttp3.*;
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
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.io.*;
import org.json.JSONObject;
public class ModifierForum {

    private final ForumService forumService = new ForumService();
    private Forum forumActuel; // Forum sélectionné pour modification
    private String imagePath = "";

    @FXML
    private TextField TFcontenue;

    @FXML
    private ImageView TFimage;

    @FXML
    private TextField TFtitre;

    @FXML
    private Button btnAfficherForum;
    private static final String API_KEY = "AIzaSyChr4M6I2yRTS3hArYsm4DjJTEi1wYi2Xc";
    /**
     * Méthode pour charger les informations d'un forum sélectionné
     */
    public void setForum(Forum forum) {
        if (forum == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Forum non trouvé.");
            return;
        }

        this.forumActuel = forum;
        TFtitre.setText(forum.getTitre());
        TFcontenue.setText(forum.getContenu());

        imagePath = forum.getImage();
        updateImageView();  // Méthode refactorisée pour mettre à jour l'image
    }

    /**
     * Modifier un forum existant
     */
    @FXML
    void modifier(ActionEvent event) {
        if (forumActuel == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un forum à modifier.");
            return;
        }

        String titre = TFtitre.getText().trim();
        String contenu = TFcontenue.getText().trim();

        if (titre.isEmpty() || contenu.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        String sanitizedTitre = isContentInappropriate(titre);
        String sanitizedContenu = isContentInappropriate(contenu);

        if (sanitizedTitre.contains("*") || sanitizedContenu.contains("*")) {
            showAlert(Alert.AlertType.ERROR, "Contenu interdit", "Votre message contient des propos inappropriés.");
        }

        // **UPLOAD IMAGE FIRST** (if new image selected)
        if (imagePath != null && !imagePath.startsWith("http")) {  // If the imagePath is not a URL
            try {
                imagePath = uploadImageToServer(new File(imagePath));  // Upload & get URL
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du téléversement de l'image.");
                return;
            }
        }

        // Update forum object with new data
        forumActuel.setTitre(sanitizedTitre);
        forumActuel.setContenu(sanitizedContenu);
        forumActuel.setImage(imagePath); // Store URL instead of file path
        forumActuel.setDateCreation(new Date(System.currentTimeMillis()));

        try {
            boolean success = forumService.update(forumActuel);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Forum modifié avec succès !");
                afficherListeForums();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La mise à jour a échoué.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Sélectionner une nouvelle image
     */
    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            imagePath = file.getAbsolutePath();
            TFimage.setImage(new Image(file.toURI().toString()));  // Mise à jour de l'image affichée
        }
    }

    /**
     * Retourner à la liste des forums
     */
    @FXML
    void afficherListeForums() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherForum.fxml"));
            Parent root = loader.load();
            btnAfficherForum.getScene().setRoot(root);  // Changer de vue
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'afficher la liste des forums : " + e.getMessage());
        }
    }

    /**
     * Met à jour l'image dans le composant ImageView
     */
    private void updateImageView() {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                TFimage.setImage(new Image(imageFile.toURI().toString()));
            }else if (imagePath.startsWith("http")){
                TFimage.setImage(new Image(imagePath));
            } else {
                // Si l'image n'existe pas, on affiche une image par défaut
                TFimage.setImage(new Image(getClass().getResource("/images/empty.png").toString()));
            }
        } else {
            // Image par défaut si aucun chemin n'est spécifié
            TFimage.setImage(new Image(getClass().getResource("/images/empty.png").toString()));
        }
    }

    /**
     * Affichage d'une alerte
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();  // Affiche l'alerte et attend la fermeture de l'utilisateur
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
