package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Modality;
import services.TransportService;
import models.Transport;
import util.WindowsNotificationUtil;
import services.PDFExportTransportService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TransportController {

    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtNomStation, txtZone, searchTransport;
    @FXML private ComboBox<String> cmbSortBy;
    @FXML private VBox transportDisplay;

    private final TransportService service = new TransportService();
    private Transport selectedTransport = null;

    @FXML
    public void initialize() {
        initializeComboBoxes();
        loadTransports();
    }

    private void initializeComboBoxes() {
        cmbType.setItems(FXCollections.observableArrayList("Bus", "Train", "Taxi"));
        cmbType.setValue("Bus");

        cmbSortBy.setItems(FXCollections.observableArrayList("Type", "Station", "Zone"));
        cmbSortBy.setValue("Type");
        cmbSortBy.setOnAction(event -> sortTransports());
    }

    private void loadTransports() {
        transportDisplay.getChildren().clear();
        List<Transport> transports = service.getAll();
        for (Transport transp : transports) {
            transportDisplay.getChildren().add(createTransportCard(transp));
        }
    }

    @FXML
    private void filterTransports() {
        String keyword = searchTransport.getText().toLowerCase().trim();
        List<Transport> transports = service.getAll().stream()
                .filter(t -> t.getType_transp().toLowerCase().contains(keyword) ||
                        t.getNom_station().toLowerCase().contains(keyword) ||
                        t.getZone_geographique().toLowerCase().contains(keyword))
                .toList();

        transportDisplay.getChildren().clear();
        for (Transport transport : transports) {
            transportDisplay.getChildren().add(createTransportCard(transport));
        }
    }

    @FXML
    private void sortTransports() {
        String sortBy = cmbSortBy.getValue();
        if (sortBy == null) return;

        List<Transport> transports = service.getAll();
        Comparator<Transport> comparator = switch (sortBy) {
            case "Type" -> Comparator.comparing(Transport::getType_transp);
            case "Station" -> Comparator.comparing(Transport::getNom_station);
            case "Zone" -> Comparator.comparing(Transport::getZone_geographique);
            default -> null;
        };

        if (comparator != null) {
            transports.sort(comparator);
        }

        transportDisplay.getChildren().clear();
        for (Transport transport : transports) {
            transportDisplay.getChildren().add(createTransportCard(transport));
        }
    }

    private HBox createTransportCard(Transport transp) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-radius: 5; -fx-border-width: 1;");

        // ✅ Affichage avec l'ID
        Text info = new Text(
                "ID: " + transp.getId_transp() +
                        " | Type: " + transp.getType_transp() +
                        " | Station: " + transp.getNom_station() +
                        " | Zone: " + transp.getZone_geographique() +
                        " | Note : " + transp.getNote()
        );

        Button btnDelete = new Button("supprimer❌");
        btnDelete.setOnAction(e -> deleteTransport(transp));

        Button btnEdit = new Button("modifier✏️");
        btnEdit.setOnAction(e -> selectTransportForEdit(transp));
        Button btnRATING = new Button("Rate");
        btnRATING.setOnAction(e -> selectTransportForRating(transp));

        card.getChildren().addAll(info, btnEdit, btnDelete,btnRATING);
        return card;
    }


    @FXML
    private void addTransport() {
        if (!validateFields()) {
            return; // 🚨 Bloque l'ajout si la validation échoue
        }

        Transport transp = new Transport(
                cmbType.getValue(),
                txtNomStation.getText().trim(),
                txtZone.getText().trim()
        );

        int newId = service.add(transp); // ✅ Récupère l'ID du transport ajouté

        loadTransports();
        clearFields();
        showAlert("Succès", "🚀 Transport ajouté avec succès !", Alert.AlertType.INFORMATION);

        // 🔔 Notification Windows avec ID
        WindowsNotificationUtil.showWindowsNotification("Ajout Transport", "Transport ID: " + newId + " ajouté !");
    }




    @FXML
    private void updateTransport() {
        if (selectedTransport == null) {
            showAlert("Erreur", "⚠ Veuillez sélectionner un transport à modifier !", Alert.AlertType.ERROR);
            return;
        }

        if (!validateFields()) {
            return; // 🚨 Bloque la modification si la validation échoue
        }

        selectedTransport.setType_transp(cmbType.getValue());
        selectedTransport.setNom_station(txtNomStation.getText().trim());
        selectedTransport.setZone_geographique(txtZone.getText().trim());

        service.update(selectedTransport);
        loadTransports();
        clearFields();
        showAlert("Succès", "✅ Transport modifié avec succès !", Alert.AlertType.INFORMATION);

        // 🔔 Notification Windows avec ID
        WindowsNotificationUtil.showWindowsNotification("Modification Transport", "Transport ID: " + selectedTransport.getId_transp() + " modifié !");
        selectedTransport = null;
    }



    @FXML
    private void deleteTransport(Transport transp) {
        if (transp == null) {
            showAlert("Erreur", "⚠ Aucun transport sélectionné !", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "🚨 Voulez-vous vraiment supprimer ce transport ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            service.delete(transp.getId_transp());
            loadTransports();
            showAlert("Succès", "✅ Transport supprimé avec succès!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void clearFields() {
        cmbType.setValue("Bus");
        txtNomStation.clear();
        txtZone.clear();
        selectedTransport = null;
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) transportDisplay.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à la page d'accueil!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void selectTransportForEdit(Transport transp) {
        if (transp == null) return;

        selectedTransport = transp;
        cmbType.setValue(transp.getType_transp());
        txtNomStation.setText(transp.getNom_station());
        txtZone.setText(transp.getZone_geographique());
    }
    @FXML
    private void selectTransportForRating(Transport transp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Rating.fxml"));
            Parent root = loader.load();
 System.out.println("clicked");
            System.out.println("transport"+transp.getNom_station());
            RatingController controller = loader.getController();
            controller.setTransport(transp, (transport, rating) -> {
                System.out.println("Rated " + transport.getNom_station() + " with " + rating);
              service.addRating(rating,transp.getId_transp());
                loadTransports();
            });

            Stage stage = new Stage();
            stage.setTitle("Rate Transport");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private boolean validateFields() {
        String nomStation = txtNomStation.getText().trim();
        String zoneGeo = txtZone.getText().trim();

        if (nomStation.isEmpty() || zoneGeo.isEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs obligatoires !", Alert.AlertType.ERROR);
            return false;
        }

        if (!nomStation.matches("^[a-zA-Z ]+$")) { // ✅ Accepte uniquement les lettres et espaces
            showAlert("Erreur de saisie", "Le nom de la station ne doit contenir que des lettres !", Alert.AlertType.ERROR);
            return false;
        }

        if (!zoneGeo.matches("^[a-zA-Z ]+$")) { // ✅ Accepte uniquement les lettres et espaces
            showAlert("Erreur de saisie", "La zone géographique ne doit contenir que des lettres !", Alert.AlertType.ERROR);
            return false;
        }

        return true; // ✅ Si tout est bon
    }



    @FXML
    private void deleteTransportFromSelection() {
        if (selectedTransport != null) {
            deleteTransport(selectedTransport);
        } else {
            showAlert("Erreur", "⚠ Sélectionnez un transport à supprimer!", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void exportTransportsPDF() {
        String filePath = System.getProperty("user.home") + "/Desktop/Transports.pdf";
        PDFExportTransportService.exportTransportsToPDF(service.getAll(), filePath);

        showAlert("Export PDF", "Le fichier PDF des transports a été généré sur le Bureau.", Alert.AlertType.INFORMATION);
    }



}
