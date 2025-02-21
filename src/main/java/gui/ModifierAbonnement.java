package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Abonnement;
import services.AbonnementService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class ModifierAbonnement {

    @FXML private TextField txtType;
    @FXML private TextField txtMontant;
    @FXML private TextField txtTransportId;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private Button btnModifier;

    private final AbonnementService service = new AbonnementService();
    private Abonnement selectedAbonnement;

    public void setSelectedAbonnement(Abonnement abonnement) {
        this.selectedAbonnement = abonnement;
        txtType.setText(abonnement.getType_abonnem());
        txtMontant.setText(String.valueOf(abonnement.getMontant()));

        if (abonnement.getDate_debut() != null) {
            dateDebut.setValue(abonnement.getDate_debut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (abonnement.getDate_fin() != null) {
            dateFin.setValue(abonnement.getDate_fin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        txtTransportId.setText(String.valueOf(abonnement.getTransport_id()));
    }

    @FXML
    private void updateAbonnement() {
        if (selectedAbonnement == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonnement à modifier!", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            selectedAbonnement.setType_abonnem(txtType.getText());
            selectedAbonnement.setMontant(Double.parseDouble(txtMontant.getText()));
            selectedAbonnement.setDate_debut(Date.valueOf(dateDebut.getValue()));
            selectedAbonnement.setDate_fin(Date.valueOf(dateFin.getValue()));
            selectedAbonnement.setTransport_id(Integer.parseInt(txtTransportId.getText()));

            service.update(selectedAbonnement);
            showAlert("Succès", "Abonnement modifié avec succès!", Alert.AlertType.INFORMATION);
        }
    }

    private boolean validateFields() {
        if (txtType.getText().isEmpty() || txtMontant.getText().isEmpty() ||
                dateDebut.getValue() == null || dateFin.getValue() == null ||
                txtTransportId.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Double.parseDouble(txtMontant.getText());
            Integer.parseInt(txtTransportId.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Montant et Transport ID doivent être numériques!", Alert.AlertType.ERROR);
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
