package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Hebergement;
import tn.esprit.services.HebergementService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierHebergement implements Initializable {

    @FXML private ImageView imageView; // 🔹 FIX: Added missing ImageView reference
    @FXML private TextField txtNom;
    @FXML private TextField txtType;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtVille;
    @FXML private TextField txtPays;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtPrix;
    @FXML private Button btnModifier;

    private Hebergement hebergement;
    private final HebergementService hebergementService = new HebergementService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImage(); // 🔹 Loads the default image on initialization
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

    /**
     * Initialise les champs avec les données de l'hébergement sélectionné.
     */
    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;

        if (hebergement != null) {
            txtNom.setText(hebergement.getNom());
            txtType.setText(hebergement.getType());
            txtAdresse.setText(hebergement.getAdresse());
            txtVille.setText(hebergement.getVille());
            txtPays.setText(hebergement.getPays());
            txtCapacite.setText(String.valueOf(hebergement.getCapacite()));
            txtPrix.setText(String.valueOf(hebergement.getPrix()));
        }
    }

    @FXML
    private void updateHebergement() {
        if (hebergement == null) return;

        // 🔹 Validate inputs
        if (!validateFields()) return;

        try {
            hebergement.setNom(txtNom.getText());
            hebergement.setType(txtType.getText());
            hebergement.setAdresse(txtAdresse.getText());
            hebergement.setVille(txtVille.getText());
            hebergement.setPays(txtPays.getText());
            hebergement.setCapacite(Integer.parseInt(txtCapacite.getText()));
            hebergement.setPrix(Integer.parseInt(txtPrix.getText()));

            hebergementService.update(hebergement);

            showAlert("Succès", "Hébergement modifié avec succès!", Alert.AlertType.INFORMATION);

            // 🔹 Close the window after modification
            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Capacité et Prix doivent être des nombres valides!", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        if (txtNom.getText().isEmpty() || txtType.getText().isEmpty() || txtAdresse.getText().isEmpty() ||
                txtVille.getText().isEmpty() || txtPays.getText().isEmpty() || txtCapacite.getText().isEmpty() ||
                txtPrix.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
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
}
