package tn.esprit.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import tn.esprit.models.Hebergement;
import tn.esprit.services.HebergementService;

import java.io.File;

public class AjouterHebergement {

    @FXML private ImageView imageView;
    @FXML private TextField txtNom;
    @FXML private TextField txtType;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtVille;
    @FXML private TextField txtPays;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtPrix;
    @FXML private Button btnSelectImage;

    private final HebergementService service = new HebergementService();
    private String imagePath = null; // Initialisé à null
    private Runnable refreshCallback; // Callback to refresh the parent view

    @FXML
    public void initialize() {
        if (btnSelectImage != null) {
            btnSelectImage.setOnAction(event -> selectImage());
        } else {
            System.err.println("⚠ Erreur : btnSelectImage est null. Vérifiez le FXML.");
        }

        loadImage();
    }

    private void loadImage() {
        String imagePath = "C:/Users/khali/IdeaProjects/GestionHebrgement/478765722_1161037295221445_2233461229557996646_n.png";
        File file = new File(imagePath);
        if (file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            System.err.println("⚠ Image file not found at: " + imagePath);
        }
    }

    @FXML
    public void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath(); // Stocker seulement le chemin
            System.out.println("Image sélectionnée : " + imagePath);
        }
    }

    @FXML
    private void addHebergement() {
        if (!validateFields()) return;

        try {
            int capacite = Integer.parseInt(txtCapacite.getText());
            int prix = Integer.parseInt(txtPrix.getText());

            Hebergement hebergement = new Hebergement(
                    txtNom.getText(),
                    txtType.getText(),
                    txtAdresse.getText(),
                    txtVille.getText(),
                    txtPays.getText(),
                    capacite,
                    prix,
                    imagePath // Stocker l'image
            );

            service.add(hebergement);
            showAlert("Succès", "Hébergement ajouté avec succès!", Alert.AlertType.INFORMATION);

            // Call the refresh callback to update the parent view
            if (refreshCallback != null) {
                refreshCallback.run();
            }

            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Capacité et Prix doivent être des nombres valides!", Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        txtNom.clear();
        txtType.clear();
        txtAdresse.clear();
        txtVille.clear();
        txtPays.clear();
        txtCapacite.clear();
        txtPrix.clear();
        imagePath = null;
    }

    private boolean validateFields() {
        if (txtNom.getText().isEmpty() || txtType.getText().isEmpty() || txtAdresse.getText().isEmpty() ||
                txtVille.getText().isEmpty() || txtPays.getText().isEmpty() || txtCapacite.getText().isEmpty() ||
                txtPrix.getText().isEmpty() || imagePath == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs et sélectionner une image!", Alert.AlertType.ERROR);
            return false;
        }

        if (!isNumeric(txtCapacite.getText()) || !isNumeric(txtPrix.getText())) {
            showAlert("Erreur", "Capacité et Prix doivent être des nombres valides!", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Set the refresh callback
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }
}