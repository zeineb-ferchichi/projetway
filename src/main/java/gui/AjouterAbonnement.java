package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Abonnement;
import services.AbonnementService;

public class AjouterAbonnement {

    @FXML private ComboBox<String> comboType;
    @FXML private TextField txtMontant;
    @FXML private TextField txtDureeValable;
    @FXML private ComboBox<String> comboStatus;
    @FXML private TextField txtTransportId;
    @FXML private Button btnAjouter;

    private final AbonnementService service = new AbonnementService();

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

    @FXML
    private void addAbonnement() {
        if (validateFields()) {
            try {
                Abonnement abo = new Abonnement(
                        0,  // L'ID est auto-généré par la base de données
                        comboType.getValue(),
                        Double.parseDouble(txtMontant.getText()),
                        Integer.parseInt(txtDureeValable.getText()),
                        Integer.parseInt(txtTransportId.getText()),
                        comboStatus.getValue()
                );
                service.add(abo);
                showAlert("Succès", "✅ Abonnement ajouté avec succès!", Alert.AlertType.INFORMATION);
                clearFields();
            } catch (Exception e) {
                showAlert("Erreur", "⚠ Vérifiez les valeurs saisies !", Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validateFields() {
        if (comboType.getValue() == null || txtMontant.getText().isEmpty() ||
                txtDureeValable.getText().isEmpty() || comboStatus.getValue() == null ||
                txtTransportId.getText().isEmpty()) {
            showAlert("Erreur", "⚠ Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    @FXML
    private void clearFields() {
        comboType.setValue("Mensuel");
        txtMontant.clear();
        txtDureeValable.clear();
        comboStatus.setValue("Actif");
        txtTransportId.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}