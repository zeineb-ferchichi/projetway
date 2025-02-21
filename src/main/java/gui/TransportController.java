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

    @FXML private ComboBox<String> cmbType; // ✅ Liste déroulante pour le type de transport
    @FXML private TextField txtNomStation, txtZone;
    @FXML private VBox transportDisplay;
    @FXML private Button btnModifier, btnSupprimer, btnAnnuler;

    private final TransportService service = new TransportService();
    private Transport selectedTransport = null;

    @FXML
    public void initialize() {
        loadTransports();
        initializeComboBox(); // ✅ Initialisation de la liste déroulante
    }

    // 🔹 Initialiser la liste déroulante avec 3 types prédéfinis
    private void initializeComboBox() {
        cmbType.getItems().addAll("Bus", "Train", "Taxi");
        cmbType.setValue("Bus"); // ✅ Définir une valeur par défaut
    }

    // 🔹 Charger les transports depuis la base de données
    private void loadTransports() {
        transportDisplay.getChildren().clear();
        List<Transport> transports = service.getAll();
        for (Transport transp : transports) {
            transportDisplay.getChildren().add(createTransportCard(transp));
        }
    }

    // 🔹 Création des cartes d'affichage des transports
    private HBox createTransportCard(Transport transp) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10; -fx-background-color: #BBDEFB; -fx-border-color: #0D47A1; -fx-border-radius: 5; -fx-border-width: 2;");

        // ✅ Affichage de l'ID et du nouveau champ `nom_station`
        Text info = new Text(
                "ID: " + transp.getId_transp() +
                        " | Type: " + transp.getType_transp() +
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

    // 🔹 Ajouter un transport
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
            showAlert("Succès", "Transport ajouté avec succès!", Alert.AlertType.INFORMATION);
        }
    }

    // 🔹 Modifier un transport existant
    @FXML
    private void updateTransport() {
        if (selectedTransport == null) {
            showAlert("Erreur", "Veuillez sélectionner un transport à modifier !", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            selectedTransport.setType_transp(cmbType.getValue());
            selectedTransport.setNom_station(txtNomStation.getText());
            selectedTransport.setZone_geographique(txtZone.getText());

            service.update(selectedTransport);
            loadTransports();
            clearFields();
            showAlert("Succès", "Transport modifié avec succès!", Alert.AlertType.INFORMATION);
            selectedTransport = null;
        }
    }

    // 🔹 Supprimer un transport
    @FXML
    private void deleteTransport(Transport transp) {
        if (transp == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce transport ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            service.delete(transp);
            loadTransports();
        }
    }

    // 🔹 Sélectionner un transport pour modification
    private void selectTransportForEdit(Transport transp) {
        selectedTransport = transp;
        cmbType.setValue(transp.getType_transp());
        txtNomStation.setText(transp.getNom_station());
        txtZone.setText(transp.getZone_geographique());
    }

    // 🔹 Effacer les champs du formulaire
    @FXML
    private void clearFields() {
        cmbType.setValue("Bus");
        txtNomStation.clear();
        txtZone.clear();
        selectedTransport = null;
    }

    // 🔹 Vérifier que tous les champs sont remplis avant d'ajouter/modifier un transport
    private boolean validateFields() {
        if (cmbType.getValue().isEmpty() || txtNomStation.getText().isEmpty() || txtZone.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    // 🔹 Afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 🔹 Retour à l'écran d'accueil
    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/Home.fxml"));
            Stage stage = (Stage) transportDisplay.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à la page d'accueil!", Alert.AlertType.ERROR);
        }
    }
}
