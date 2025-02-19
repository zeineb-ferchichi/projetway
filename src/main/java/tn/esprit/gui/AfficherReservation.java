package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.models.Reservation;
import tn.esprit.services.ReservationService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherReservation implements Initializable {

    @FXML private TableView<Reservation> tableReservations;
    @FXML private TableColumn<Reservation, String> clientCol;
    @FXML private TableColumn<Reservation, String> dateDebutCol;
    @FXML private TableColumn<Reservation, String> dateFinCol;
    @FXML private TableColumn<Reservation, Integer> hebergementCol;
    @FXML private TableColumn<Reservation, Void> actionsCol;
    @FXML private Button btnRefresh;
    @FXML private Button btnAjouter;

    private final ReservationService reservationService = new ReservationService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configuration des colonnes
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        hebergementCol.setCellValueFactory(new PropertyValueFactory<>("hebergementId"));

        // Ajouter des boutons "Modifier" et "Supprimer" dans chaque ligne
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button editButton = new Button("Modifier");

            {
                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null) {
                        supprimerReservation(reservation);
                    }
                });

                editButton.setOnAction(event -> {
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null) {
                        openModifierReservation(reservation);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new javafx.scene.layout.HBox(5, editButton, deleteButton));
                }
            }
        });

        // Charger les données dans la table
        loadReservations();
    }

    private void loadReservations() {
        tableReservations.getItems().clear();
        List<Reservation> reservations = reservationService.getAll();
        tableReservations.getItems().addAll(reservations);
    }

    private void supprimerReservation(Reservation reservation) {
        reservationService.delete(reservation.getId());
        loadReservations(); // Rafraîchir la liste après suppression
    }

    @FXML
    private void handleRefresh() {
        loadReservations(); // Recharger les réservations
    }

    @FXML
    private void handleAjouterReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            newStage.setTitle("Ajouter une Réservation");
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openModifierReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Scene scene = new Scene(loader.load());

            ModifierReservation controller = loader.getController();
            controller.setReservation(reservation); // Passer les données à la fenêtre de modification

            Stage newStage = new Stage();
            newStage.setTitle("Modifier une Réservation");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
