package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        Integer id = spinnerIdTransport.getValue();
        if (id == null || id <= 0) {
            showAlert("Erreur", "⚠ Veuillez entrer un ID valide !", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le transport ?");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce transport ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean isDeleted = service.delete(id); // ✅ Appel correct de `delete(int id)`
            if (isDeleted) {
                showAlert("Succès", "✅ Transport supprimé avec succès !", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "❌ Échec de la suppression du transport !", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Méthode pour afficher des alertes.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
