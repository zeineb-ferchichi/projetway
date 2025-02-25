package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.Transport;
import services.TransportService;

import java.util.List;

public class AfficherTransport {

    @FXML private ListView<String> listTransports;
    @FXML private Button btnRetour;

    private final TransportService service = new TransportService();

    @FXML
    public void initialize() {
        loadTransports();
    }

    private void loadTransports() {
        List<Transport> transports = service.getAll();
        listTransports.getItems().clear();

        for (Transport transp : transports) {
            String details = "ID: " + transp.getId_transp() +
                    " | Type: " + transp.getType_transp() +
                    " | Station: " + transp.getNom_station() +
                    " | Zone: " + transp.getZone_geographique();
            listTransports.getItems().add(details);
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }
}