package gui;

import entities.voyage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import servies.voyageservice;

import java.io.IOException;
import java.time.LocalDate;

public class ajoutervoyage {

    private final voyageservice voyageservice = new voyageservice();

    @FXML
    private ComboBox<voyage.Destination> comboDestination; // Dropdown for destinations

    @FXML
    private DatePicker dpDateDepart; // Date picker for departure

    @FXML
    private DatePicker dpDateRetour; // Date picker for return

    @FXML
    private Label lblError; // Label to show validation errors

    @FXML
    public void initialize() {
        // Populate ComboBox with destination options
        comboDestination.getItems().addAll(voyage.Destination.values());
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            voyage.Destination destination = comboDestination.getValue();
            LocalDate dateDepart = dpDateDepart.getValue();
            LocalDate dateRetour = dpDateRetour.getValue();

            // Validate input
            if (destination == null || dateDepart == null || dateRetour == null) {
                lblError.setText("Veuillez remplir tous les champs.");
                return;
            }
            if (!dateDepart.isBefore(dateRetour)) {
                lblError.setText("La date de départ doit être avant la date de retour.");
                return;
            }

            voyage voyage = new voyage(destination, dateDepart, dateRetour);
            voyageservice.add(voyage);
            lblError.setText("Voyage ajouté avec succès !");
        } catch (Exception e) {
            lblError.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    void afficher(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/affichervoyage.fxml"));
            dpDateDepart.getScene().setRoot(root);
        } catch (IOException e) {
            lblError.setText("Erreur: " + e.getMessage());
        }
    }

}
