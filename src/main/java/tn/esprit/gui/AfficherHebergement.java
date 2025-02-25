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
import java.util.stream.Collectors;

public class AfficherHebergement {

    @FXML private TableView<Hebergement> listHebergements;
    @FXML private TableColumn<Hebergement, String> nomCol;
    @FXML private TableColumn<Hebergement, String> typeCol;
    @FXML private TableColumn<Hebergement, String> adresseCol;
    @FXML private TableColumn<Hebergement, String> villeCol;
    @FXML private TableColumn<Hebergement, String> paysCol;
    @FXML private TableColumn<Hebergement, Integer> capaciteCol;
    @FXML private TableColumn<Hebergement, Integer> prixCol;
    @FXML private TableColumn<Hebergement, Void> actionsCol;
    @FXML private ImageView imageView;

    @FXML private Button btnAjouter;
    @FXML private Button btnRefresh;
    @FXML private TextField searchField; // Ajout du champ de recherche
    @FXML private Button searchButton; // Bouton de recherche

    private final HebergementService hebergementService = new HebergementService();

    @FXML
    public void initialize() {
        if (listHebergements == null) {
            System.err.println("Error: listHebergements is null. Check FXML fx:id!");
            return;
        }

        // Charger l'image d'en-tête
        loadImage();

        // Liaison des colonnes aux propriétés des objets Hebergement
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        villeCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
        paysCol.setCellValueFactory(new PropertyValueFactory<>("pays"));
        capaciteCol.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

        // Ajout des boutons Modifier et Supprimer
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

        // Charger la liste des hébergements
        loadHebergements();

        // Ajouter l'action de recherche
        searchButton.setOnAction(event -> handleSearch());
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

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadHebergements(); // Recharger tous les hébergements si le champ est vide
            return;
        }

        List<Hebergement> filteredList = hebergementService.getAll().stream()
                .filter(h -> h.getPays().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        listHebergements.getItems().clear();
        listHebergements.getItems().addAll(filteredList);
    }

    private void deleteHebergement(Hebergement hebergement) {
        hebergementService.delete(hebergement.getId()); // Supprimer l'hébergement
        loadHebergements(); // Rafraîchir la liste
    }

    private void openModifierHebergement(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
            Scene scene = new Scene(loader.load());

            ModifierHebergement controller = loader.getController();
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
        loadHebergements();
    }

    @FXML
    private void handleAjouterHebergement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHebergement.fxml"));
            Scene scene = new Scene(loader.load());

            AjouterHebergement ajouterController = loader.getController();
            ajouterController.setHebergementTableView(listHebergements);

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
