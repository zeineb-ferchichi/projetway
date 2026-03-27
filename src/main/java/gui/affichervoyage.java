package gui;

import entities.voyage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import servies.voyageservice;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class affichervoyage {

    private final voyageservice voyageservice = new voyageservice();
    private ObservableList<voyage> voyageList;

    @FXML
    private ListView<voyage> listVoyages;
    @FXML
    private TextField searchField;
    @FXML
    private Button sortButton; // Button to sort voyages

    @FXML
    public void initialize() {
        loadVoyages();
        setupSearch();
        setupSorting();
    }

    private void loadVoyages() {
        List<voyage> voyages = voyageservice.getAll();
        voyageList = FXCollections.observableArrayList(voyages);
        listVoyages.setItems(voyageList);

        listVoyages.setCellFactory(param -> new ListCell<voyage>() {
            @Override
            protected void updateItem(voyage v, boolean empty) {
                super.updateItem(v, empty);

                if (empty || v == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label details = new Label(v.getDestination() + " - " + v.getDate_depart() + " → " + v.getDate_retour());
                    setGraphic(details);
                }
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<voyage> filteredList = voyageservice.getAll().stream()
                    .filter(v -> v.getDestination().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());
            listVoyages.setItems(FXCollections.observableArrayList(filteredList));
        });
    }

    private void setupSorting() {
        sortButton.setOnAction(e -> sortVoyagesByName());
    }

    private void sortVoyagesByName() {
        List<voyage> sortedList = voyageList.stream()
                .sorted(Comparator.comparing(v -> v.getDestination().name())) // Convert enum to String
                .collect(Collectors.toList());

        listVoyages.setItems(FXCollections.observableArrayList(sortedList));
    }

}
