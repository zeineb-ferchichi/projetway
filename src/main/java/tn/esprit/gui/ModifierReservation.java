package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.cell.TextFieldListCell;
import tn.esprit.models.Reservation;
import tn.esprit.services.ReservationService;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifierReservation {

    @FXML private ImageView imageView;
    @FXML private TextField txtClientName;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private final ReservationService reservationService = new ReservationService();
    private Reservation reservation;

    @FXML
    public void initialize() {
        loadImage();
        configureDatePickers();
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
        dateDebut.setValue(convertToLocalDate(reservation.getDateDebut()));
        dateFin.setValue(convertToLocalDate(reservation.getDateFin()));
    }

    @FXML
    private void modifierReservation() {
        if (reservation != null) {
            if (txtClientName.getText().isEmpty() || dateDebut.getValue() == null || dateFin.getValue() == null) {
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

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
