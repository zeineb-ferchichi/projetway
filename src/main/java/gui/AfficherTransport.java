package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Transport;
import services.TransportService;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AfficherTransport {

    @FXML private ListView<Transport> listTransports;
    @FXML private Button btnRetour;
    private final TransportService service = new TransportService();

    @FXML
    public void initialize() {
        loadTransports();
        setupListView();
    }

    private void loadTransports() {
        try {
            List<Transport> transports = service.getAll();
            ObservableList<Transport> transportList = FXCollections.observableArrayList(transports);
            listTransports.setItems(transportList);
        } catch (Exception e) {
            showError("Erreur lors du chargement des transports", e.getMessage());
        }
    }

    private void setupListView() {
        // Utilisation d'un StringConverter pour afficher proprement les objets Transport
        listTransports.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Transport transport, boolean empty) {
                super.updateItem(transport, empty);
                if (empty || transport == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText("🚍 " + transport.getType_transp() + " - " + transport.getNom_station() + " (" + transport.getZone_geographique() + ")");
                    Tooltip tooltip = new Tooltip("Type: " + transport.getType_transp() +
                            "\nStation: " + transport.getNom_station() +
                            "\nZone: " + transport.getZone_geographique());
                    setTooltip(tooltip);
                }
            }
        });
    }

    @FXML
    private void retour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
