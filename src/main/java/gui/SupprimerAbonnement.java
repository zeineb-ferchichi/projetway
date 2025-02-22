package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.AbonnementService;

import java.util.Optional;

public class SupprimerAbonnement {

    @FXML private TextField txtIdAbonnement;
    @FXML private Button btnSupprimer;
    @FXML private Button btnAnnuler;

    private final AbonnementService service = new AbonnementService();

    @FXML
    private void deleteAbonnement() {
        String idText = txtIdAbonnement.getText().trim();

        if (idText.isEmpty()) {
            showAlert("Erreur", "⚠ Veuillez entrer un ID!", Alert.AlertType.ERROR);
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            if (service.getById(id) == null) {
                showAlert("Erreur", "⚠ Aucun abonnement trouvé avec cet ID!", Alert.AlertType.ERROR);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer l'abonnement ?");
            alert.setContentText("Voulez-vous vraiment supprimer cet abonnement ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = service.delete(id);
                if (success) {
                    showAlert("Succès", "✅ Abonnement supprimé avec succès!", Alert.AlertType.INFORMATION);
                    clearFields();
                } else {
                    showAlert("Erreur", "❌ Échec de la suppression de l'abonnement!", Alert.AlertType.ERROR);
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Erreur", "⚠ ID invalide! Veuillez entrer un nombre.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearFields() {
        txtIdAbonnement.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
