package tn.esprit.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Hebergement;
import tn.esprit.models.Reservation;
import tn.esprit.services.HebergementService;
import tn.esprit.services.ReservationService;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterReservation implements Initializable {

    @FXML private TextField txtClientName;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private ComboBox<Hebergement> comboHebergement;
    @FXML private Button btnAjouter;

    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadHebergements();
    }

    private void loadHebergements() {
        List<Hebergement> hebergements = hebergementService.getAll();
        ObservableList<Hebergement> hebergementList = FXCollections.observableArrayList(hebergements);
        comboHebergement.setItems(hebergementList);
    }

    @FXML
    private void ajouterReservation() {
        String clientName = txtClientName.getText();
        LocalDate debut = dateDebut.getValue();
        LocalDate fin = dateFin.getValue();
        Hebergement selectedHebergement = comboHebergement.getValue();

        if (clientName.isEmpty() || debut == null || fin == null || selectedHebergement == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (debut.isAfter(fin)) {
            showAlert(Alert.AlertType.ERROR, "Date invalide", "La date de début doit être avant la date de fin.");
            return;
        }

        Reservation reservation = new Reservation(clientName, Date.valueOf(debut), Date.valueOf(fin), selectedHebergement.getId());
        reservationService.add(reservation);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès!");

        // Fermer la fenêtre après l'ajout
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
