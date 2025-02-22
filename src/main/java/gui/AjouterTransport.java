package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Transport;
import services.TransportService;

public class AjouterTransport {

    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtNomStation;
    @FXML private TextField txtZone;
    @FXML private Button btnAjouter;

    private final TransportService service = new TransportService();

    @FXML
    public void initialize() {
        cmbType.getItems().addAll("Bus", "Train", "Taxi");
        cmbType.setValue("Bus");
    }

    @FXML
    private void addTransport() {
        if (validateFields()) {
            Transport transp = new Transport(
                    cmbType.getValue(),
                    txtNomStation.getText(),
                    txtZone.getText()
            );
            service.add(transp);
            showAlert("Succès", "Transport ajouté avec succès!", Alert.AlertType.INFORMATION);
            clearFields();
        }
    }

    private boolean validateFields() {
        return !(cmbType.getValue().isEmpty() || txtNomStation.getText().isEmpty() || txtZone.getText().isEmpty());
    }

    @FXML
    private void clearFields() {
        cmbType.setValue("Bus");
        txtNomStation.clear();
        txtZone.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
