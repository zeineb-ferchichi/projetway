package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Transport;
import services.TransportService;

public class ModifierTransport {

    @FXML private ComboBox<String> comboType;
    @FXML private TextField txtStation;
    @FXML private TextField txtZone;
    private Transport selectedTransport;
    private final TransportService service = new TransportService();

    @FXML
    public void initialize() {
        // Initialiser la ComboBox avec les types de transport disponibles
        comboType.getItems().addAll("Bus", "Train", "Taxi");
    }

    public void setSelectedTransport(Transport transport) {
        this.selectedTransport = transport;
        if (transport != null) {
            comboType.setValue(transport.getType_transp());
            txtStation.setText(transport.getNom_station());
            txtZone.setText(transport.getZone_geographique());
        }
    }

    @FXML
    private void updateTransport() {
        if (selectedTransport == null) {
            showAlert("Erreur", "Veuillez sélectionner un transport à modifier !", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            selectedTransport.setType_transp(comboType.getValue());
            selectedTransport.setNom_station(txtStation.getText());
            selectedTransport.setZone_geographique(txtZone.getText());

            service.update(selectedTransport);
            showAlert("Succès", "🚀 Transport modifié avec succès !", Alert.AlertType.INFORMATION);
        }
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
