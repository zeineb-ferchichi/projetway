package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Transport;
import services.TransportService;

import java.util.Optional;

public class SupprimerTransport {

    @FXML private Spinner<Integer> spinnerIdTransport;
    private final TransportService service = new TransportService();

    @FXML
    public void initialize() {
        spinnerIdTransport.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1));
    }

    @FXML
    private void deleteTransport() {
        int id = spinnerIdTransport.getValue();
        Transport transport = service.getById(id);

        if (transport == null) {
            showAlert("Erreur", "Aucun transport trouvé avec cet ID!", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer ce transport?");
        alert.setContentText("Voulez-vous vraiment supprimer ce transport?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.delete(transport);
            showAlert("Succès", "Transport supprimé avec succès!", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
