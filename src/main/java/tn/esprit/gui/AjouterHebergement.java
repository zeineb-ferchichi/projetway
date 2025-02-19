package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Hebergement;
import tn.esprit.services.HebergementService;

public class AjouterHebergement {

    @FXML private TextField txtNom;
    @FXML private TextField txtType;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtVille;
    @FXML private TextField txtPays;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtPrix;

    private final HebergementService service = new HebergementService();

    @FXML
    private void addHebergement() {
        if (validateFields()) {
            try {
                Hebergement hebergement = new Hebergement(
                        txtNom.getText(),
                        txtType.getText(),
                        txtAdresse.getText(),
                        txtVille.getText(),
                        txtPays.getText(),
                        Integer.parseInt(txtCapacite.getText()),
                        Integer.parseInt(txtPrix.getText())
                );

                service.add(hebergement);
                showAlert("Succès", "Hébergement ajouté avec succès!", Alert.AlertType.INFORMATION);
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Capacité et Prix doivent être des nombres valides!", Alert.AlertType.ERROR);
            }
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
    }

    private boolean validateFields() {
        if (txtNom.getText().isEmpty() || txtType.getText().isEmpty() || txtAdresse.getText().isEmpty() ||
                txtVille.getText().isEmpty() || txtPays.getText().isEmpty() || txtCapacite.getText().isEmpty() ||
                txtPrix.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Integer.parseInt(txtCapacite.getText());
            Integer.parseInt(txtPrix.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Capacité et Prix doivent être des nombres valides!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
