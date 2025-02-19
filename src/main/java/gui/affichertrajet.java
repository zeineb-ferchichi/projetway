package gui;

import entities.trajet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import servies.trajetservice;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class affichertrajet implements Initializable {

    @FXML
    private ListView<String> listViewTrajets; // ListView to display trajets

    private final trajetservice trajetService = new trajetservice();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewTrajets.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
        loadTrajets(); // Load data when initialized
    }

    private void loadTrajets() {
        List<trajet> trajets = trajetService.getAll(); // Get all trajets from database
        ObservableList<String> observableTrajets = FXCollections.observableArrayList();

        for (trajet t : trajets) {
            observableTrajets.add("🚌 Transport: " + t.getType_transport() +
                    "\n🏢 Compagnie: " + t.getCompagnie() +
                    "\n📅 Départ: " + t.getDate_depart() +
                    "\n📍 Ville Départ: " + t.getVille_depart() +
                    "\n📍 Ville Arrivée: " + t.getVille_arrivee() +
                    "\n💰 Coût: " + t.getCout() + " €");
        }
        listViewTrajets.setItems(observableTrajets);
    }
}
