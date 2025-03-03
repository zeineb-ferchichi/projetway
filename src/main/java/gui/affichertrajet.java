package gui;

import entities.trajet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import servies.TrajetService;
import java.util.List;
import java.util.stream.Collectors;

public class affichertrajet {

    private final TrajetService trajetservice = new TrajetService();
    private ObservableList<trajet> trajetList;

    @FXML
    private ListView<HBox> listTrajets; // Correction du type pour correspondre à l'affichage
    @FXML
    private TextField searchField;
    @FXML
    private Button sortButton;

    @FXML
    public void initialize() {
        loadTrajets();
        setupSearch();
        setupSorting();
    }

    private void loadTrajets() {
        List<trajet> trajets = trajetservice.getAll(); // Correction de l'appel au service
        ObservableList<HBox> items = FXCollections.observableArrayList();

        for (trajet t : trajets) {
            Label label = new Label(t.getType_transport() + " | " + t.getVille_depart() + " -> " + t.getVille_arrivee());
            HBox hbox = new HBox(10, label);
            items.add(hbox);
        }

        listTrajets.setItems(items);
        trajetList = FXCollections.observableArrayList(trajets); // Initialisation correcte
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (trajetList == null || trajetList.isEmpty()) return;

            List<trajet> filteredList = trajetList.stream()
                    .filter(t -> t.getVille_depart().toLowerCase().contains(newValue.toLowerCase()) ||
                            t.getType_transport().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());

            ObservableList<HBox> filteredItems = FXCollections.observableArrayList();
            for (trajet t : filteredList) {
                Label label = new Label(t.getType_transport() + " | " + t.getVille_depart() + " -> " + t.getVille_arrivee());
                filteredItems.add(new HBox(10, label));
            }

            listTrajets.setItems(filteredItems);
        });
    }
    private void setupSorting() {
        sortButton.setOnAction(e -> sortTrajetsByCost());
    }
    private void sortTrajetsByCost() {
        if (trajetList == null || trajetList.isEmpty()) {
            System.out.println("La liste des trajets est vide ou non initialisée !");
            return;
        }

        List<trajet> sortedList = trajetList.stream()
                .sorted((t1, t2) -> Double.compare(t2.getCout(), t1.getCout())) // Sort by cost (highest first)
                .collect(Collectors.toList());

        ObservableList<HBox> sortedItems = FXCollections.observableArrayList();
        for (trajet t : sortedList) {
            Label label = new Label(t.getType_transport() + " | " + t.getVille_depart() + " -> " + t.getVille_arrivee() + " | Coût: " + t.getCout() + "€");
            sortedItems.add(new HBox(10, label));
        }

        listTrajets.setItems(sortedItems);
    }

}
