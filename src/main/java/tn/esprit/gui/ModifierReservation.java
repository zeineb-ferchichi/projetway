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
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierReservation implements Initializable {

    @FXML private ImageView imageView;
    @FXML private TextField txtClientName;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;
    @FXML private ComboBox<Hebergement> comboHebergement;

    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();
    private Reservation reservation;

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
        comboHebergement.setConverter(new StringConverter<Hebergement>() {
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

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        txtClientName.setText(reservation.getClientName());

        // Explicitly convert java.util.Date to java.sql.Date for conversion to LocalDate
        java.sql.Date dateDebutSql = new java.sql.Date(reservation.getDateDebut().getTime());
        java.sql.Date dateFinSql = new java.sql.Date(reservation.getDateFin().getTime());

        // Convert java.sql.Date to LocalDate
        dateDebut.setValue(convertToLocalDate(dateDebutSql));
        dateFin.setValue(convertToLocalDate(dateFinSql));

        // Pré-sélectionner l'hébergement associé à la réservation
        hebergementService.getAll().stream()
                .filter(h -> h.getId() == reservation.getHebergementId())
                .findFirst()
                .ifPresent(comboHebergement::setValue);
    }

    @FXML
    private void modifierReservation() {
        if (reservation != null) {
            if (txtClientName.getText().isEmpty() || dateDebut.getValue() == null || dateFin.getValue() == null || comboHebergement.getValue() == null) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            if (dateDebut.getValue().isAfter(dateFin.getValue())) {
                showAlert("Date invalide", "La date de début doit être avant la date de fin.");
                return;
            }

            reservation.setClientName(txtClientName.getText());
            reservation.setDateDebut(convertToDate(dateDebut.getValue()));
            reservation.setDateFin(convertToDate(dateFin.getValue()));
            reservation.setHebergementId(comboHebergement.getValue().getId());

            reservationService.update(reservation);
            showAlert("Succès", "Réservation modifiée avec succès !");

            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private java.sql.Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate); // Correct conversion from LocalDate to java.sql.Date
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}