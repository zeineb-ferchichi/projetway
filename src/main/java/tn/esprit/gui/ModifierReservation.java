package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
