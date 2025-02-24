package tn.esprit.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.models.Hebergement;
import tn.esprit.models.Reservation;
import tn.esprit.services.HebergementService;
import tn.esprit.services.ReservationService;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterReservation implements Initializable {

    @FXML private ImageView imageView;
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
        loadImage();
        configureDatePickers();
    }

    private void loadHebergements() {
        List<Hebergement> hebergements = hebergementService.getAll();
        ObservableList<Hebergement> hebergementList = FXCollections.observableArrayList(hebergements);
        comboHebergement.setItems(hebergementList);

        // Afficher uniquement le nom de l'hébergement dans le ComboBox
        comboHebergement.setConverter(new StringConverter<>() {
            @Override
            public String toString(Hebergement hebergement) {
                return (hebergement != null) ? hebergement.getNom() : "";
            }

            @Override
            public Hebergement fromString(String string) {
                return hebergementList.stream()
                        .filter(h -> h.getNom().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void loadImage() {
        String imagePath = "C:/Users/khali/IdeaProjects/GestionHebrgement/478765722_1161037295221445_2233461229557996646_n.png";
        File file = new File(imagePath);
        if (file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            System.err.println("⚠ Image file not found at: " + imagePath);
        }
    }

    private void configureDatePickers() {
        // Empêcher la sélection de dates passées pour dateDebut
        dateDebut.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        // Empêcher la sélection d'une date de fin avant la date de début
        dateDebut.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dateFin.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isBefore(newValue.plusDays(1))); // Date fin doit être après date début
                    }
                });
            }
        });
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
