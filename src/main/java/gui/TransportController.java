package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Transport;
import services.TransportService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TransportController {

    @FXML private ComboBox<String> cmbType;
    @FXML private TextField txtNomStation, txtZone;
    @FXML private VBox transportDisplay;

    private final TransportService service = new TransportService();
    private Transport selectedTransport = null;

    @FXML
    public void initialize() {
        loadTransports();
        initializeComboBox();
    }

    private void initializeComboBox() {
        cmbType.getItems().addAll("Bus", "Train", "Taxi");
        cmbType.setValue("Bus");
    }

    private void loadTransports() {
        transportDisplay.getChildren().clear();
        List<Transport> transports = service.getAll();
        for (Transport transp : transports) {
            transportDisplay.getChildren().add(createTransportCard(transp));
        }
    }

    private HBox createTransportCard(Transport transp) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10; -fx-border-color: #0D47A1; -fx-border-radius: 5; -fx-border-width: 2;");

        Text info = new Text(
                "ID: " + transp.getId_transp() +
                        " | Type: " + transp.getType_transp() +
                        " | Station: " + transp.getNom_station() +
                        " | Zone: " + transp.getZone_geographique()
        );

        Button btnDelete = new Button("❌");
        btnDelete.setOnAction(e -> deleteSelectedTransport(transp)); // Correction de la méthode

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
            selectedTransport = null;
        }
    }

    @FXML
    private void deleteSelectedTransport(Transport transp) { // Correction du nom de la méthode
        if (transp == null || transp.getId_transp() <= 0) {
            showAlert("Erreur", "⚠ Aucun transport sélectionné ou ID invalide !", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("🚨 Voulez-vous vraiment supprimer ce transport ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            boolean success = service.delete(transp); // Correction pour retourner un boolean
            if (success) {
                loadTransports();
                showAlert("Succès", "🚮 Transport supprimé avec succès !", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "❌ Échec de la suppression du transport !", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void selectTransportForEdit(Transport transp) {
        selectedTransport = transp;
        cmbType.setValue(transp.getType_transp());
        txtNomStation.setText(transp.getNom_station());
        txtZone.setText(transp.getZone_geographique());
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
            showAlert("Erreur", "❌ Impossible de retourner à la page d'accueil !", Alert.AlertType.ERROR);
        }
    }

    private boolean validateFields() {
        return !(cmbType.getValue().isEmpty() || txtNomStation.getText().isEmpty() || txtZone.getText().isEmpty());
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
