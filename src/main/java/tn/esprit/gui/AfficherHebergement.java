package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.models.Hebergement;
import tn.esprit.services.HebergementService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AfficherHebergement {

    @FXML private TableView<Hebergement> listHebergements;
    @FXML private TableColumn<Hebergement, String> nomCol;
    @FXML private TableColumn<Hebergement, String> typeCol;
    @FXML private TableColumn<Hebergement, String> adresseCol;
    @FXML private TableColumn<Hebergement, String> villeCol;
    @FXML private TableColumn<Hebergement, String> paysCol;
    @FXML private TableColumn<Hebergement, Integer> capaciteCol;
    @FXML private TableColumn<Hebergement, Double> prixCol;
    @FXML private TableColumn<Hebergement, Void> actionsCol;
    @FXML private ImageView imageView;

    @FXML private Button btnAjouter;
    @FXML private Button btnRefresh;

    private final HebergementService hebergementService = new HebergementService();

    @FXML
    public void initialize() {
        if (listHebergements == null) {
            System.err.println("Error: listHebergements is null. Check FXML fx:id!");
            return;
        }

        // Load Image for UI
        loadImage();

        // Bind columns to Hebergement properties
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        villeCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
        paysCol.setCellValueFactory(new PropertyValueFactory<>("pays"));
        capaciteCol.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Add "Modifier" and "Supprimer" buttons to each row
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("🗑 Supprimer");
            private final Button editButton = new Button("✏ Modifier");
            private final HBox buttonContainer = new HBox(10, editButton, deleteButton);

            {
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
                editButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

                deleteButton.setOnAction(event -> {
                    Hebergement hebergement = getTableRow().getItem();
                    if (hebergement != null) {
                        deleteHebergement(hebergement);
                    }
                });

                editButton.setOnAction(event -> {
                    Hebergement hebergement = getTableRow().getItem();
                    if (hebergement != null) {
                        openModifierHebergement(hebergement);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonContainer);
            }
        });

        // Load initial data
        loadHebergements();
    }

    private void loadImage() {
        String imagePath = "C:/Users/khali/IdeaProjects/GestionHebrgement/478765722_1161037295221445_2233461229557996646_n.png";
        File file = new File(imagePath);
        if (file.exists()) {
            imageView.setImage(new Image(file.toURI().toString()));
        } else {
            System.err.println("⚠ Image file not found at: " + imagePath);
        }
    }

    private void loadHebergements() {
        listHebergements.getItems().clear();
        List<Hebergement> hebergements = hebergementService.getAll();
        listHebergements.getItems().addAll(hebergements);
    }

    private void deleteHebergement(Hebergement hebergement) {
        hebergementService.delete(hebergement.getId()); // Delete the Hebergement
        loadHebergements(); // Refresh the list
    }

    private void openModifierHebergement(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
            Scene scene = new Scene(loader.load());

            ModifierHebergement controller = loader.getController();
            // Pass the TableView reference and a callback to refresh the list
            controller.setHebergement(hebergement, listHebergements, this::refreshHebergements);

            Stage newStage = new Stage();
            newStage.setTitle("Modifier un Hébergement");
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshHebergements() {
        loadHebergements(); // Reload all the Hebergements from the service
    }

    @FXML
    private void handleAjouterHebergement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHebergement.fxml"));
            Scene scene = new Scene(loader.load());

            Stage newStage = new Stage();
            newStage.setTitle("Ajouter un Hébergement");
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadHebergements();
    }
}
