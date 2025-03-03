package gui;

import entities.voyage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import servies.voyageservice;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class affichervoy {

    @FXML
    private ListView<HBox> voyageListView;

    @FXML
    private Button sortButton;  // Button for sorting

    @FXML
    private BarChart<String, Number> voyageBarChart;  // BarChart for sorted display by destination

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    private final voyageservice voyageService = new voyageservice();

    @FXML
    public void initialize() {
        loadVoyages();
    }

    private void loadVoyages() {
        List<voyage> voyages = voyageService.getAll();
        ObservableList<HBox> items = FXCollections.observableArrayList();

        for (voyage v : voyages) {
            items.add(createVoyageBox(v));
        }

        voyageListView.setItems(items);
    }

    private HBox createVoyageBox(voyage v) {
        Label label = new Label(v.getDestination() + " | " + v.getDate_depart() + " -> " + v.getDate_retour());
        Button modifyBtn = new Button("Modifier");
        Button deleteBtn = new Button("Supprimer");

        modifyBtn.setOnAction(e -> modifyVoyage(v));
        deleteBtn.setOnAction(e -> deleteVoyage(v));

        HBox hbox = new HBox(10, label, modifyBtn, deleteBtn);
        return hbox;
    }

    private void modifyVoyage(voyage v) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);

        // DatePickers for departure and return dates
        DatePicker departDatePicker = new DatePicker(v.getDate_depart());
        DatePicker retourDatePicker = new DatePicker(v.getDate_retour());

        Button saveButton = new Button("Enregistrer");

        saveButton.setOnAction(e -> {
            v.setDate_depart(departDatePicker.getValue());
            v.setDate_retour(retourDatePicker.getValue());
            voyageService.update(v);
            loadVoyages();
            stage.close();
        });

        vbox.getChildren().addAll(
                new Label("Date de départ"), departDatePicker,
                new Label("Date de retour"), retourDatePicker,
                saveButton
        );

        Scene scene = new Scene(vbox, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void deleteVoyage(voyage v) {
        voyageService.delete(v.getIdvoyage());
        loadVoyages(); // Refresh ListView
    }

    // Sort the voyages by destination and display them in the BarChart
    @FXML
    private void sortVoyagesByDestination() {
        List<voyage> voyages = voyageService.getAll();

        // Count the number of voyages per destination
        Map<String, Integer> destinationCountMap = new HashMap<>();
        for (voyage v : voyages) {
            String destination = v.getDestination().name();
            destinationCountMap.put(destination, destinationCountMap.getOrDefault(destination, 0) + 1);
        }

        // Prepare the data for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Voyages par destination");

        // Add each destination and its count to the series
        destinationCountMap.forEach((destination, count) -> {
            series.getData().add(new XYChart.Data<>(destination, count));
        });

        // Set the data to the BarChart
        voyageBarChart.getData().clear();
        voyageBarChart.getData().add(series);
    }
}
