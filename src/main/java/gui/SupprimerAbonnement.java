package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Abonnement;
import services.AbonnementService;

import java.util.Optional;

public class SupprimerAbonnement {

    @FXML private TextField txtIdAbonnement;
    private final AbonnementService service = new AbonnementService();

    @FXML
    private void deleteAbonnement() {
        try {
            int id = Integer.parseInt(txtIdAbonnement.getText());
            Abonnement abonnement = service.getById(id);

            if (abonnement != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Supprimer l'abonnement ?");
                alert.setContentText("Voulez-vous vraiment supprimer cet abonnement ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    service.delete(abonnement);
                    showAlert("Succès", "Abonnement supprimé avec succès!", Alert.AlertType.INFORMATION);
                    txtIdAbonnement.clear();
                }
            } else {
                showAlert("Erreur", "Aucun abonnement trouvé avec cet ID!", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID invalide! Veuillez entrer un nombre.", Alert.AlertType.ERROR);
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
