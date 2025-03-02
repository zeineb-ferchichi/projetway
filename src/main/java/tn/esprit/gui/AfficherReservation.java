package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.models.Hebergement;
import tn.esprit.models.Reservation;
import tn.esprit.services.HebergementService;
import tn.esprit.services.ReservationService;
import tn.esprit.util.PDFGenerator;
import tn.esprit.util.QRCodeGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AfficherReservation implements Initializable {

    @FXML private TableView<Reservation> tableReservations;
    @FXML private TableColumn<Reservation, String> clientCol;
    @FXML private TableColumn<Reservation, String> dateDebutCol;
    @FXML private TableColumn<Reservation, String> dateFinCol;
    @FXML private TableColumn<Reservation, String> hebergementCol;
    @FXML private TableColumn<Reservation, Void> actionsCol;
    @FXML private Button btnRefresh;
    @FXML private Button btnAjouter;
    @FXML private ImageView imageViewQRCode;
    @FXML private ImageView imageView;
    @FXML private TextField searchField;

    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();
    private List<Reservation> allReservations;
    private ObservableList<Reservation> filteredReservations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Affichage d'une image par défaut
        Image image = new Image("file:/C:/Users/khali/IdeaProjects/GestionHebrgement/478765722_1161037295221445_2233461229557996646_n.png");
        imageView.setImage(image);

        // Initialisation des colonnes de la TableView
        clientCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        hebergementCol.setCellValueFactory(cellData -> {
            int hebergementId = cellData.getValue().getHebergementId();
            Hebergement hebergement = hebergementService.getById(hebergementId);
            return new javafx.beans.property.SimpleStringProperty(hebergement != null ? hebergement.getNom() : "Inconnu");
        });

        // Initialiser les actions pour modifier ou supprimer une réservation
        actionsCol.setCellFactory(createActionsCellFactory());

        // Charger les réservations au démarrage
        loadReservations();

        // Listener to generate QR code on row selection
        tableReservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Generate and display the QR code for the selected reservation
                displayQRCode(newValue);
            }
        });
    }

    private void loadReservations() {
        allReservations = reservationService.getAll();
        filteredReservations = FXCollections.observableArrayList(allReservations);
        tableReservations.setItems(filteredReservations);
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

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadReservations(); // Reload all reservations if the search field is empty
            return;
        }

        List<Reservation> filteredList = allReservations.stream()
                .filter(reservation -> reservation.getClientName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        filteredReservations.setAll(filteredList); // Update the ObservableList with the filtered list
        tableReservations.setItems(filteredReservations); // Update the TableView
    }

    private void displayQRCode(Reservation reservation) {
        String reservationDetails = "Client: " + reservation.getClientName() + "\n" +
                "Début: " + reservation.getDateDebut() + "\n" +
                "Fin: " + reservation.getDateFin() + "\n" +
                "Hébergement: " + hebergementService.getById(reservation.getHebergementId()).getNom();

        Image qrCodeImage = QRCodeGenerator.generateQRCodeImage(reservationDetails);

        if (qrCodeImage != null) {
            System.out.println("QR Code généré avec succès.");
            imageViewQRCode.setImage(qrCodeImage);
        } else {
            System.out.println("Erreur : QR Code non généré.");
        }
    }

    @FXML
    private void AfficherHebergement() {
        ouvrirFenetre("/AfficherHebergement.fxml", "Afficher les Hébergements");
    }

    @FXML
    private void AfficherReservation() {
        ouvrirFenetre("/AfficherReservation.fxml", "Afficher les Réservations");
    }

    @FXML
    private void quitter() {
        System.exit(0);
    }

    private void ouvrirFenetre(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(titre);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePDF(Reservation reservation) {
        if (reservation != null) {
            // Générer le PDF dans un emplacement spécifique
            String filePath = "C:/Users/khali/Documents/reservation_ticket.pdf";
            PDFGenerator.generatePDF(reservation, filePath);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Généré");
            alert.setHeaderText(null);
            alert.setContentText("Le ticket de réservation a été généré avec succès : " + filePath);
            alert.showAndWait();
        } else {
            // Afficher un message d'erreur si aucune réservation n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune Réservation Sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une réservation pour générer le PDF.");
            alert.showAndWait();
        }
    }

    public Callback<TableColumn<Reservation, Void>, TableCell<Reservation, Void>> createActionsCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final Button pdfButton = new Button("PDF");

            {
                // Styles des boutons
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");
                pdfButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

                // Actions des boutons
                editButton.setOnAction(event -> {
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null) {
                        openModifierReservation(reservation);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null) {
                        supprimerReservation(reservation);
                    }
                });

                pdfButton.setOnAction(event -> {
                    Reservation reservation = getTableRow().getItem();
                    if (reservation != null) {
                        handlePDF(reservation);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton, pdfButton));
                }
            }
        };
    }
}