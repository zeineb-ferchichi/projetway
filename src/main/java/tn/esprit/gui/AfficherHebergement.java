package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Hebergement;
import tn.esprit.services.HebergementService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherHebergement {

    @FXML private TilePane gridHebergements; // Grid for displaying hébergements
    @FXML private ImageView imageView; // Header image
    @FXML private Button btnAjouter; // Add button
    @FXML private TextField searchField; // Search field
    @FXML private Button searchButton; // Search button
    @FXML private Button sortPriceButton; // Sort by price button
    @FXML private Pagination pagination; // Pagination component

    private final HebergementService hebergementService = new HebergementService();
    private boolean ascendingOrder = true;
    private List<Hebergement> allHebergements; // Store all hébergements for pagination
    private static final int ITEMS_PER_PAGE = 8; // Number of items per page

    @FXML
    public void initialize() {
        if (gridHebergements == null) {
            System.err.println("Error: gridHebergements is null. Check FXML fx:id!");
            return;
        }

        // Load the header image
        loadImage();

        // Load the list of hébergements into the grid
        loadHebergements();

        // Add search action
        searchButton.setOnAction(event -> handleSearch());
        sortPriceButton.setOnAction(event -> handleSortByPrice());

        // Initialize pagination
        pagination.setPageFactory(this::createPage);
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
        allHebergements = hebergementService.getAll(); // Load all hébergements
        int pageCount = (int) Math.ceil((double) allHebergements.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0); // Start at the first page
    }

    private VBox createPage(int pageIndex) {
        gridHebergements.getChildren().clear(); // Clear the grid

        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allHebergements.size());

        List<Hebergement> pageHebergements = allHebergements.subList(fromIndex, toIndex);
        for (Hebergement hebergement : pageHebergements) {
            VBox gridItem = createGridItem(hebergement);
            gridHebergements.getChildren().add(gridItem);
        }

        return new VBox(gridHebergements);
    }

    private VBox createGridItem(Hebergement hebergement) {
        VBox item = new VBox(0);
        item.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px; -fx-border-radius: 5px; -fx-border-color: #ccc;");

        // Add image
        ImageView itemImageView = new ImageView();
        itemImageView.setFitWidth(150);
        itemImageView.setFitHeight(100);
        if (hebergement.getImage() != null) {
            File imageFile = new File(hebergement.getImage());
            if (imageFile.exists()) {
                itemImageView.setImage(new Image(imageFile.toURI().toString()));
            }
        }

        // Add labels for details
        Label nameLabel = new Label(hebergement.getNom());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label villePaysLabel = new Label("Ville: " + hebergement.getVille() + ", Pays: " + hebergement.getPays());
        villePaysLabel.setStyle("-fx-font-size: 12px;");

        Label priceLabel = new Label("Prix: " + hebergement.getPrix() + " DT");
        priceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4CAF50;");

        // Add buttons for actions
        HBox buttonContainer = new HBox(10);
        Button deleteButton = new Button("🗑 Supprimer");
        Button editButton = new Button("✏ Modifier");

        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        editButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        deleteButton.setOnAction(event -> deleteHebergement(hebergement));
        editButton.setOnAction(event -> openModifierHebergement(hebergement));

        buttonContainer.getChildren().addAll(editButton, deleteButton);

        // Add all elements to the VBox
        item.getChildren().addAll(itemImageView, nameLabel, villePaysLabel, priceLabel, buttonContainer);
        return item;
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadHebergements(); // Reload all hébergements if the search field is empty
            return;
        }

        List<Hebergement> filteredList = hebergementService.getAll().stream()
                .filter(h -> h.getPays().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        allHebergements = filteredList;
        int pageCount = (int) Math.ceil((double) allHebergements.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0); // Reset to the first page
    }

    private void deleteHebergement(Hebergement hebergement) {
        hebergementService.delete(hebergement.getId()); // Delete the hébergement
        loadHebergements(); // Refresh the grid
    }

    private void openModifierHebergement(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
            Scene scene = new Scene(loader.load());

            ModifierHebergement controller = loader.getController();
            controller.setHebergement(hebergement, this::refreshHebergements); // Pass the callback

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
            ajouterController.setRefreshCallback(this::refreshHebergements);

            Stage newStage = new Stage();
            newStage.setTitle("Ajouter un Hébergement");
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSortByPrice() {
        // Trier la liste complète des hébergements
        allHebergements.sort((h1, h2) -> ascendingOrder
                ? Integer.compare(h1.getPrix(), h2.getPrix())
                : Integer.compare(h2.getPrix(), h1.getPrix()));

        // Inverser l'ordre pour le prochain tri
        ascendingOrder = !ascendingOrder;

        // Rafraîchir la pagination pour afficher la liste triée
        int currentPage = pagination.getCurrentPageIndex();
        pagination.setPageCount((int) Math.ceil((double) allHebergements.size() / ITEMS_PER_PAGE));
        pagination.setCurrentPageIndex(currentPage); // Recharger la page actuelle

        // Forcer le rafraîchissement de la page actuelle
        pagination.setCurrentPageIndex(currentPage); // Cette ligne force le rechargement
    }

    private boolean isAlreadySorted() {
        // Vérifier si la liste est déjà triée dans l'ordre actuel
        for (int i = 1; i < allHebergements.size(); i++) {
            int comparison = Integer.compare(allHebergements.get(i - 1).getPrix(), allHebergements.get(i).getPrix());
            if ((ascendingOrder && comparison > 0) || (!ascendingOrder && comparison < 0)) {
                return false; // La liste n'est pas triée dans l'ordre actuel
            }
        }
        return true; // La liste est déjà triée dans l'ordre actuel
    }

    @FXML
    private void AfficherHebergement() {
        ouvrirFenetre("/AfficherHebergement.fxml", "Afficher les Hébergements");
    }

    @FXML
    private void AfficherReservation() {
        ouvrirFenetre("/AfficherReservation.fxml", "Afficher les Réservations");
    }

    @FXML
    private void quitter() {
        System.exit(0);
    }

    private void ouvrirFenetre(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(titre);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}