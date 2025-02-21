package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Abonnement;
import services.AbonnementService;

import java.sql.Date;

public class AjouterAbonnement {

    @FXML private TextField txtType;
    @FXML private TextField txtMontant;
    @FXML private TextField txtTransportId;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private Button btnAjouter;

    private final AbonnementService service = new AbonnementService();

    @FXML
    private void addAbonnement() {
        if (validateFields()) {
            Abonnement abo = new Abonnement(
                    txtType.getText(),
                    Double.parseDouble(txtMontant.getText()),
                    Date.valueOf(dateDebut.getValue()),
                    Date.valueOf(dateFin.getValue()),
                    Integer.parseInt(txtTransportId.getText())
            );
            service.add(abo);
            showAlert("Succès", "Abonnement ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        }
    }

    private boolean validateFields() {
        if (txtType.getText().isEmpty() || txtMontant.getText().isEmpty() ||
                dateDebut.getValue() == null || dateFin.getValue() == null ||
                txtTransportId.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void clearFields() {
        txtType.clear();
        txtMontant.clear();
        txtTransportId.clear();
        dateDebut.setValue(null);
        dateFin.setValue(null);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
