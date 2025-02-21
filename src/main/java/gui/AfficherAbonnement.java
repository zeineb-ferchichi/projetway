package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.Abonnement;
import services.AbonnementService;

import java.util.List;

public class AfficherAbonnement {

    @FXML private ListView<String> listAbonnements;
    @FXML private Button btnRetour;

    private final AbonnementService service = new AbonnementService();

    @FXML
    public void initialize() {
        loadAbonnements();
    }

    private void loadAbonnements() {
        List<Abonnement> abonnements = service.getAll();
        listAbonnements.getItems().clear();

        for (Abonnement abo : abonnements) {
            String details = "ID: " + abo.getId_abonnem() +
                    " | Type: " + abo.getType_abonnem() +
                    " | Montant: " + abo.getMontant() +
                    " | Date Début: " + abo.getDate_debut() +
                    " | Date Fin: " + abo.getDate_fin() +
                    " | Transport ID: " + abo.getTransport_id();
            listAbonnements.getItems().add(details);
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }
}
