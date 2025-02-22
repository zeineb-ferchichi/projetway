package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Abonnement;
import services.AbonnementService;

public class ModifierAbonnement {

    @FXML private ComboBox<String> comboType;
    @FXML private TextField txtMontant;
    @FXML private TextField txtDureeValable;
    @FXML private ComboBox<String> comboStatus;
    @FXML private TextField txtTransportId;
    @FXML private Button btnModifier;

    private final AbonnementService service = new AbonnementService();
    private Abonnement selectedAbonnement;

    @FXML
    public void initialize() {
        loadComboBoxes();
    }

    /**
     * Remplit les listes déroulantes (Type d'Abonnement et Statut).
     */
    private void loadComboBoxes() {
        comboType.getItems().addAll("Mensuel", "Annuel", "Hebdomadaire");
        comboType.setValue("Mensuel");

        comboStatus.getItems().addAll("Actif", "Expiré", "Suspendu");
        comboStatus.setValue("Actif");
    }

    /**
     * Remplit les champs avec les informations de l'abonnement sélectionné.
     */
    public void setSelectedAbonnement(Abonnement abonnement) {
        this.selectedAbonnement = abonnement;
        comboType.setValue(abonnement.getType_abonnem());
        txtMontant.setText(String.valueOf(abonnement.getMontant()));
        txtDureeValable.setText(String.valueOf(abonnement.getDuree_valable()));
        comboStatus.setValue(abonnement.getStatus_abonnem());
        txtTransportId.setText(String.valueOf(abonnement.getTransport_id()));
    }

    @FXML
    private void updateAbonnement() {
        if (selectedAbonnement == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonnement à modifier!", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            selectedAbonnement.setType_abonnem(comboType.getValue());
            selectedAbonnement.setMontant(Double.parseDouble(txtMontant.getText()));
            selectedAbonnement.setDuree_valable(Integer.parseInt(txtDureeValable.getText()));
            selectedAbonnement.setTransport_id(Integer.parseInt(txtTransportId.getText()));
            selectedAbonnement.setStatus_abonnem(comboStatus.getValue());

            service.update(selectedAbonnement);
            showAlert("Succès", "✅ Abonnement modifié avec succès!", Alert.AlertType.INFORMATION);
        }
    }

    private boolean validateFields() {
        if (comboType.getValue() == null || txtMontant.getText().isEmpty() ||
                txtDureeValable.getText().isEmpty() || comboStatus.getValue() == null ||
                txtTransportId.getText().isEmpty()) {
            showAlert("Erreur", "⚠ Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Double.parseDouble(txtMontant.getText());
            Integer.parseInt(txtDureeValable.getText());
            Integer.parseInt(txtTransportId.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "⚠ Montant, Durée et Transport ID doivent être numériques!", Alert.AlertType.ERROR);
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
