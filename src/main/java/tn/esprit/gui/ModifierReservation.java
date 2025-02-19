package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Reservation;
import tn.esprit.services.ReservationService;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ModifierReservation {

    @FXML private TextField txtClientName;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private Button btnModifier;
    @FXML private Button btnAnnuler;

    private final ReservationService reservationService = new ReservationService();
    private Reservation reservation;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        txtClientName.setText(reservation.getClientName());
        dateDebut.setValue(convertToLocalDate(reservation.getDateDebut()));
        dateFin.setValue(convertToLocalDate(reservation.getDateFin()));
    }

    @FXML
    private void modifierReservation() {
        if (reservation != null) {
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
