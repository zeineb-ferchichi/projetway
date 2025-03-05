package gui;

import entities.voyage;
import entities.trajet; // Assure-toi d'importer aussi la classe trajet
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
import gui.EmailService;

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
        Button emailBtn = new Button("Envoyer Email");

        modifyBtn.setOnAction(e -> modifyVoyage(v));
        deleteBtn.setOnAction(e -> deleteVoyage(v));
        emailBtn.setOnAction(e -> envoyerEmail(v)); // Envoie l'email pour ce voyage

        return new HBox(10, label, modifyBtn, deleteBtn, emailBtn);
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

            // Appeler l'envoi d'email après modification
            envoyerEmail(v);  // Envoie l'email avec les informations modifiées

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
        loadVoyages(); // Rafraîchit la ListView

        // Appeler l'envoi d'email après suppression
        envoyerEmail(v);  // Envoie l'email après la suppression du voyage
    }

    // Fonction pour envoyer un email à l'employé après modification ou suppression d'un voyage
    private void envoyerEmail(voyage v) {
        List<trajet> trajets = v.getTrajets();
        System.out.println("Trajets: " + trajets);  // Ajoute cette ligne pour vérifier si trajets est null

        if (trajets == null || trajets.isEmpty()) {
            System.out.println("Aucun trajet associé à ce voyage.");
            return; // Ne pas envoyer l'email si les trajets sont vides
        }

        String employeEmail = "mouhamarzoukk70@gmail.com"; // Remplace par l'email de l'employé
        String nomEmploye = "Nom de l'employé"; // Remplace par le nom de l'employé
        EmailService.envoyerEmailEmploye(employeEmail, nomEmploye, trajets);
    }

    // Tri des voyages par destination et affichage dans le BarChart
    @FXML
    private void sortVoyagesByDestination() {
        List<voyage> voyages = voyageService.getAll();

        // Compter le nombre de voyages par destination
        Map<String, Integer> destinationCountMap = new HashMap<>();
        for (voyage v : voyages) {
            String destination = String.valueOf(v.getDestination()); // Assure-toi que getDestination() retourne bien une chaîne
            destinationCountMap.put(destination, destinationCountMap.getOrDefault(destination, 0) + 1); // Incrémentation par 1
        }

        // Préparer les données pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Voyages par destination");

        // Ajouter chaque destination et son compte dans la série
        destinationCountMap.forEach((destination, count) -> {
            series.getData().add(new XYChart.Data<>(destination, count));
        });

        // Définir les données pour le BarChart
        voyageBarChart.getData().clear();
        voyageBarChart.getData().add(series);
    }
}
