package gui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import entities.trajet;
import servies.TrajetService;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.CostCalculator;

public class affichertables {

    @FXML
    private ListView<HBox> trajetListView;

    @FXML
    private Button sumButton;  // Button for calculating the sum of costs

    @FXML
    private Label sumLabel;    // Label for displaying the sum of costs

    private final TrajetService trajetService = new TrajetService();
    private final CostCalculator costCalculator = new CostCalculator();

    @FXML
    public void initialize() {
        loadTrajets();

    }

    private void loadTrajets() {
        List<trajet> trajets = trajetService.getAll();
        ObservableList<HBox> items = FXCollections.observableArrayList();

        for (trajet t : trajets) {
            Label label = new Label(t.getType_transport() + " | " + t.getVille_depart() + " -> " + t.getVille_arrivee());
            Button modifyBtn = new Button("Modifier");
            Button deleteBtn = new Button("Supprimer");

            modifyBtn.setOnAction(e -> modifyTrajet(t));
            deleteBtn.setOnAction(e -> deleteTrajet(t));

            HBox hbox = new HBox(10, label, modifyBtn, deleteBtn);
            items.add(hbox);
        }
        trajetListView.setItems(items);
    }

    private void modifyTrajet(trajet t) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);
        TextField transportField = new TextField(t.getType_transport());
        TextField departField = new TextField(t.getVille_depart());
        TextField arriveeField = new TextField(t.getVille_arrivee());
        Button saveButton = new Button("Enregistrer");

        saveButton.setOnAction(e -> {
            t.setType_transport(transportField.getText());
            t.setVille_depart(departField.getText());
            t.setVille_arrivee(arriveeField.getText());
            trajetService.update(t);
            loadTrajets();
            stage.close();
        });

        vbox.getChildren().addAll(new Label("Type Transport"), transportField, new Label("Ville Départ"), departField, new Label("Ville Arrivée"), arriveeField, saveButton);
        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void deleteTrajet(trajet t) {
        trajetService.delete(t.getId_trajet());
        loadTrajets(); // Refresh ListView
    }

    // Calculate the sum of costs for all trajets and display it in the label
    @FXML
    private void calculateSumOfCosts() {
        List<trajet> trajets = trajetService.getAll();
        double totalCost = 0.0;

        // Assuming each trajet has a "getCost()" method, you can sum the costs.
        for (trajet t : trajets) {
            totalCost += t.getCout(); // Replace getCost() with the actual method for retrieving cost.
        }

        // Update the label to display the sum of the costs
        sumLabel.setText("Somme des Coûts: " + totalCost);
    }

    @FXML
    private Button totalCostButton;  // Button for calculating the total cost in USD

    @FXML
    private void calculateTotalCost() {
        List<trajet> trajets = trajetService.getAll(); // Get the list of all trajets
        double totalCost = costCalculator.calculateTotalCost(trajets);
        sumLabel.setText("Total Cost in USD: " + totalCost);
    }

}
