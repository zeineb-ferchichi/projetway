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
import tn.esprit.util.QRCodeGenerator;  // Classe pour générer le QR Code

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
    @FXML private ImageView imageViewQRCode; // ImageView pour afficher le QR code
    @FXML private ImageView imageView; // L'ImageView de gauche pour l'image

    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Affichage d'une image par défaut
        Image image = new Image("file:/C:/Users/khali/IdeaProjects/GestionHebrgement/478765722_1161037295221445_2233461229557996646_n.png");
        imageView.setImage(image);

        // Ajouter un listener pour la sélection dans la table
        tableReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Afficher le QR Code lorsque la réservation est sélectionnée
                displayQRCode(newValue);
            }
        });

        // Initialisation des colonnes de la TableView
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        // Afficher le nom d'hébergement dans la TableView
        hebergementCol.setCellValueFactory(cellData -> {
            int hebergementId = cellData.getValue().getHebergementId();
            Hebergement hebergement = hebergementService.getById(hebergementId);
            return new javafx.beans.property.SimpleStringProperty(hebergement != null ? hebergement.getNom() : "Inconnu");
        });

        // Initialiser les actions pour modifier ou supprimer une réservation
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
            private final Button editButton = new Button("Modifier");

            {
                // Style des boutons
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");

                // Actions des boutons
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

        // Charger les réservations au démarrage
        loadReservations();
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
        // Ouvrir la fenêtre de modification d'une réservation
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Scene scene = new Scene(loader.load());

            ModifierReservation controller = loader.getController();
            controller.setReservation(reservation);
            controller.setReservationTableView(tableReservations);

            Stage newStage = new Stage();
            newStage.setTitle("Modifier la Réservation");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayQRCode(Reservation reservation) {
        String reservationDetails = "Client: " + reservation.getClientName() + "\n" +
                "Début: " + reservation.getDateDebut() + "\n" +
                "Fin: " + reservation.getDateFin() + "\n" +
                "Hébergement: " + hebergementService.getById(reservation.getHebergementId()).getNom();

        Image qrCodeImage = QRCodeGenerator.generateQRCodeImage(reservationDetails);

        if (qrCodeImage != null) {
            System.out.println("QR Code généré avec succès.");
            imageViewQRCode.setImage(qrCodeImage); // Afficher l'image dans l'ImageView
        } else {
            System.out.println("Erreur : QR Code non généré.");
        }
    }


}
