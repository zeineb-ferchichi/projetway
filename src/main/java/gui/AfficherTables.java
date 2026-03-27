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
import gui.CostCalculator; // Ajout de l'IA

public class affichertables {

    @FXML
    private ListView<HBox> trajetListView;

    @FXML
    private Button sumButton;  // Bouton pour calculer la somme des coûts

    @FXML
    private Label sumLabel;    // Label pour afficher la somme

    @FXML
    private Button totalCostButton;  // Bouton pour coût en USD

    @FXML
    private Button aiCostButton;  // Bouton pour coût via IA

    private final TrajetService trajetService = new TrajetService();
    private final CostCalculator costCalculator = new CostCalculator();
    private final CostCalculator aiCostCalculator = new CostCalculator(); // Instance IA

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

    @FXML
    private void calculateSumOfCosts() {
        List<trajet> trajets = trajetService.getAll();
        double totalCost = 0.0;

        for (trajet t : trajets) {
            totalCost += t.getCout();
        }

        sumLabel.setText("Somme des Coûts: " + totalCost);
    }

    @FXML
    private void calculateTotalCost() {
        List<trajet> trajets = trajetService.getAll();
        double totalCost = costCalculator.calculateTotalCostUsingAI(trajets);
        sumLabel.setText("Total Cost in USD: " + totalCost);
    }

    // 📌 Nouveau bouton pour l'IA
    @FXML
    private void calculateTotalCostUsingAI() {
        List<trajet> trajets = trajetService.getAll();
        double estimatedCost = aiCostCalculator.calculateTotalCostUsingAI(trajets);
        sumLabel.setText("Total Cost (AI): " + estimatedCost);
    }
}
