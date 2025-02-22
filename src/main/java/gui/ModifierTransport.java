package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Transport;
import services.TransportService;

public class ModifierTransport {

    @FXML private TextField txtNomStation;
    @FXML private TextField txtZone;
    @FXML private ComboBox<String> cmbType;
    @FXML private Button btnModifier;

    private final TransportService service = new TransportService();
    private Transport selectedTransport;

    public void setSelectedTransport(Transport transport) {
        this.selectedTransport = transport;
        cmbType.setValue(transport.getType_transp());
        txtNomStation.setText(transport.getNom_station());
        txtZone.setText(transport.getZone_geographique());
    }

    @FXML
    private void updateTransport() {
        if (selectedTransport == null) {
            showAlert("Erreur", "Aucun transport sélectionné!", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            selectedTransport.setType_transp(cmbType.getValue());
            selectedTransport.setNom_station(txtNomStation.getText());
            selectedTransport.setZone_geographique(txtZone.getText());

            service.update(selectedTransport);
            showAlert("Succès", "Transport mis à jour avec succès!", Alert.AlertType.INFORMATION);
        }
    }

    private boolean validateFields() {
        return !(cmbType.getValue().isEmpty() || txtNomStation.getText().isEmpty() || txtZone.getText().isEmpty());
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
