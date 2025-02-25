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
import services.TransportService;
import models.Transport;
import util.WindowsNotificationUtil;

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

        Text info = new Text(
                "Type: " + transp.getType_transp() +
                        " | Station: " + transp.getNom_station() +
                        " | Zone: " + transp.getZone_geographique()
        );

        Button btnDelete = new Button("❌");
        btnDelete.setOnAction(e -> deleteTransport(transp));

        Button btnEdit = new Button("✏️");
        btnEdit.setOnAction(e -> selectTransportForEdit(transp));

        card.getChildren().addAll(info, btnEdit, btnDelete);
        return card;
    }

    @FXML
    private void addTransport() {
        if (validateFields()) {
            Transport transp = new Transport(
                    cmbType.getValue(),
                    txtNomStation.getText(),
                    txtZone.getText()
            );
            service.add(transp);
            loadTransports();
            clearFields();
            showAlert("Succès", "🚀 Transport ajouté avec succès !", Alert.AlertType.INFORMATION);

            WindowsNotificationUtil.showWindowsNotification("Ajout Transport", "Le transport " + transp.getNom_station() + " a été ajouté !");
        } else {
            showAlert("Erreur", "⚠ Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateTransport() {
        if (selectedTransport == null) {
            showAlert("Erreur", "⚠ Veuillez sélectionner un transport à modifier !", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            selectedTransport.setType_transp(cmbType.getValue());
            selectedTransport.setNom_station(txtNomStation.getText());
            selectedTransport.setZone_geographique(txtZone.getText());

            service.update(selectedTransport);
            loadTransports();
            clearFields();
            showAlert("Succès", "✅ Transport modifié avec succès !", Alert.AlertType.INFORMATION);

            WindowsNotificationUtil.showWindowsNotification("Modification Transport", "Le transport " + selectedTransport.getNom_station() + " a été modifié !");
            selectedTransport = null;
        }
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

    private boolean validateFields() {
        return !txtNomStation.getText().isEmpty() && !txtZone.getText().isEmpty();
    }
    @FXML
    private void deleteTransportFromSelection() {
        if (selectedTransport != null) {
            deleteTransport(selectedTransport);
        } else {
            showAlert("Erreur", "⚠ Sélectionnez un transport à supprimer!", Alert.AlertType.ERROR);
        }
    }

}
