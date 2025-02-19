package gui;

import entities.voyage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import servies.voyageservice;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class affichervoyage implements Initializable {

    @FXML
    private ListView<String> listViewVoyages; // ListView to display voyages

    private final voyageservice voyageService = new voyageservice();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewVoyages.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
        loadVoyages(); // Load data when initialized
    }

    private void loadVoyages() {
        List<voyage> voyages = voyageService.getAll(); // Get all voyages from database
        ObservableList<String> observableVoyages = FXCollections.observableArrayList();

        for (voyage v : voyages) {
            observableVoyages.add("Destination: " + v.getDestination() +
                    " | Départ: " + v.getDate_depart() +
                    " | Retour: " + v.getDate_retour());
        }
        listViewVoyages.setItems(observableVoyages);
    }
}
