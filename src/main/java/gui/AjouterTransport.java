package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Transport;
import services.TransportService;

public class AjouterTransport {

    @FXML private ComboBox<String> comboType;
    @FXML private TextField txtStation;
    @FXML private TextField txtZone;

    private final TransportService service = new TransportService();

    @FXML
    public void initialize() {
        // Initialiser la ComboBox avec les types de transport
        comboType.getItems().addAll("Bus", "Train", "Taxi");
    }

    @FXML
    private void addTransport() {
        if (validateFields()) {
            Transport transport = new Transport(
                    comboType.getValue(),
                    txtStation.getText(),
                    txtZone.getText()
            );
            service.add(transport);
            showAlert("Succès", "🚀 Transport ajouté avec succès !", Alert.AlertType.INFORMATION);
            clearFields();
        }
    }

    private void clearFields() {
        comboType.getSelectionModel().clearSelection();
        txtStation.clear();
        txtZone.clear();
    }

    private boolean validateFields() {
        if (comboType.getValue() == null || txtStation.getText().isEmpty() || txtZone.getText().isEmpty()) {
            showAlert("Erreur", "⚠ Veuillez remplir tous les champs !", Alert.AlertType.ERROR);
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
