package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Hebergement;
import tn.esprit.models.Reservation;
import tn.esprit.services.HebergementService;
import tn.esprit.services.ReservationService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherReservation implements Initializable {

    @FXML private TableView<Reservation> tableReservations;
    @FXML private TableColumn<Reservation, String> clientCol;
    @FXML private TableColumn<Reservation, String> dateDebutCol;
    @FXML private TableColumn<Reservation, String> dateFinCol;
    @FXML private TableColumn<Reservation, String> hebergementCol;
    @FXML private TableColumn<Reservation, Void> actionsCol;
    @FXML private Button btnRefresh;
    @FXML private Button btnAjouter;
    @FXML private ImageView imageView; // Ajout de l'ImageView pour afficher l'image

    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des colonnes
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        // Modifier la colonne pour afficher le NOM de l'hébergement au lieu de l'ID
        hebergementCol.setCellValueFactory(cellData -> {
            int hebergementId = cellData.getValue().getHebergementId();
            Hebergement hebergement = hebergementService.getById(hebergementId);
            return new javafx.beans.property.SimpleStringProperty(hebergement != null ? hebergement.getNom() : "Inconnu");
        });

        // Initialiser les actions (Modifier, Supprimer)
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
                setGraphic(empty ? null : new javafx.scene.layout.HBox(5, editButton, deleteButton));
            }
        });

        // Charger les réservations
        loadReservations();

        // Charger l'image (exemple de chemin)
        loadImage();
    }

    private void loadImage() {
        String imagePath = "C:/Users/khali/IdeaProjects/GestionHebrgement/478765722_1161037295221445_2233461229557996646_n.png"; // Remplacez ce chemin par le bon
        File file = new File(imagePath);
        if (file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            System.err.println("⚠ Image file not found at: " + imagePath);
        }
    }

    private void loadReservations() {
        tableReservations.getItems().setAll(reservationService.getAll());
    }

    private void supprimerReservation(Reservation reservation) {
        reservationService.delete(reservation.getId());
        loadReservations();
    }

    @FXML
    private void handleRefresh() {
        loadReservations();
    }

    @FXML
    private void handleAjouterReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Scene scene = new Scene(loader.load());

            AjouterReservation controller = loader.getController();
            controller.setReservationTableView(tableReservations);

            Stage newStage = new Stage();
            newStage.setTitle("Ajouter une Réservation");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openModifierReservation(Reservation reservation) {
        // Modifier la réservation ici
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Scene scene = new Scene(loader.load());

            ModifierReservation controller = loader.getController();
            controller.setReservation(reservation);
            controller.setReservationTableView(tableReservations); // Passer la référence de la TableView

            Stage newStage = new Stage();
            newStage.setTitle("Modifier la Réservation");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
