package gui;

import entities.Mission;
import entities.Rapport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.MissionService;
import services.RapportService;
import gui.ModifierRapportController;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import gui.ModifierRapportController;
import gui.ModifierRapportController;
import gui.ModifierRapportController;

public class AfficherRapportController {
    @FXML
    private GridPane gridRapports;

    private Rapport rapportSelectionne;
    private final RapportService rapportService = new RapportService();
    @FXML
    private TextField searchRapportField;
    @FXML
    private TextField TFNomRapport;

    @FXML
    private DatePicker DPDateExp;

    @FXML
    private Button btnAjouterRapport;
    @FXML
    private Button btnSupprimerRapport;
    @FXML
    private Button btnModifierRapport;
    @FXML
    private Button btnRafraichir;

    private final ObservableList<Rapport> rapportList = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbMissions;
    private final MissionService missionService = new MissionService();

    @FXML
    private void initialize() {
        rafraichirListe();
        loadRapports();
        rapportList.setAll(rapportService.getAll());

        // Charger les missions disponibles
        List<Mission> missions = missionService.getAll();
        ObservableList<String> missionNames = FXCollections.observableArrayList();

        for (Mission mission : missions) {
            missionNames.add(mission.getNomMission());
        }

        cbMissions.setItems(missionNames);

    }
    // ✅ Supprimer un rapport sélectionné
    @FXML
    private void supprimerRapport(Rapport rapport) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer ce rapport ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                rapportService.delete(rapport);
                showAlert("Succès", "Rapport supprimé !");
                rafraichirListe();
            }
        });
    }
    private void loadRapports() {
        gridRapports.getChildren().clear();
        rapportList.setAll(rapportService.getAll());

        afficherRapports(rapportList);
    }
    @FXML
    private void filtrerRapports() {
        String searchText = searchRapportField.getText().toLowerCase();
        ObservableList<Rapport> filteredList = FXCollections.observableArrayList();

        for (Rapport rapport : rapportList) {
            if (rapport.getLibelleR().toLowerCase().contains(searchText) ||
                    rapport.getDateExpo().toString().contains(searchText)) {
                filteredList.add(rapport);
            }
        }

        afficherRapports(filteredList);
    }
    private void afficherRapports(ObservableList<Rapport> rapports) {
        gridRapports.getChildren().clear();
        int rowIndex = 1;

        for (Rapport rapport : rapports) {
            gridRapports.add(new Label(String.valueOf(rapport.getIdRapport())), 0, rowIndex);
            gridRapports.add(new Label(rapport.getLibelleR()), 1, rowIndex);
            gridRapports.add(new Label(rapport.getDateExpo().toString()), 2, rowIndex);

            Button btnModifier = new Button("📝 Modifier");
            btnModifier.setOnAction(event -> modifierRapport(rapport));
            btnModifier.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");
            gridRapports.add(btnModifier, 3, rowIndex);

            Button btnSupprimer = new Button("🗑 Supprimer");
            btnSupprimer.setOnAction(event -> supprimerRapport(rapport));
            btnSupprimer.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");
            gridRapports.add(btnSupprimer, 4, rowIndex);

            rowIndex++;
        }
    }
    private Rapport rapport;

    public void setRapport(Rapport rapport) {
        this.rapport = rapport;
        remplirChamps();  // Remplit les champs du formulaire
    }
    private void modifierRapport(Rapport rapport) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierRapport.fxml"));
            Parent root = loader.load();

            ModifierRapportController controller = loader.getController();
            controller.setRapport(rapport);  // Assurez-vous que cette méthode existe dans ModifierRapportController

            Stage stage = new Stage();
            stage.setTitle("Modifier Rapport");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadRapports(); // Rafraîchir la liste après modification
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification du rapport.");
        }
    }

    private void remplirChamps() {
        if (rapport != null) {
            TFNomRapport.setText(rapport.getLibelleR());
            DPDateExp.setValue(rapport.getDateExpo());
        }
    }




    @FXML
    private void ouvrirFichier(String chemin) {
        if (chemin == null || chemin.isEmpty()) {
            showAlert("Erreur", "Aucun fichier sélectionné !");
            return;
        }

        File fichier = new File(chemin);

        if (!fichier.exists()) {
            showAlert("Erreur", "Fichier introuvable !");
            return;
        }

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(fichier);
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir le fichier !");
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur", "L'ouverture de fichiers n'est pas supportée sur ce système !");
        }
    }
    @FXML
    private void ouvrirFichier() { // Méthode utilisée dans le FXML
        showAlert("Erreur", "Veuillez sélectionner un fichier avant d'ouvrir !");
    }





    @FXML
    public void rafraichirListe() {
        gridRapports.getChildren().clear();  // 🔄 Supprime les anciens rapports avant de recharger

        List<Rapport> rapports = rapportService.getAll();
        int row = 1;

        for (Rapport rapport : rapports) {
            gridRapports.add(new Label(String.valueOf(rapport.getIdRapport())), 0, row);
            gridRapports.add(new Label(rapport.getLibelleR()), 1, row);
            gridRapports.add(new Label(rapport.getDateExpo().toString()), 2, row);
            gridRapports.add(new Label(String.join(", ", rapport.getRessources())), 3, row);
            gridRapports.add(new Label(rapport.getMission().getNomMission()), 4, row);

            // ✅ Ajouter le bouton "Sélectionner"
            Button btnSelectionner = new Button("✔ Sélectionner");
            btnSelectionner.setOnAction(event -> {
                rapportSelectionne = rapport;  // ✅ Enregistre le rapport sélectionné
                System.out.println("📌 Rapport sélectionné : " + rapportSelectionne.getLibelleR());

                // ✅ Met à jour visuellement la sélection
                for (javafx.scene.Node node : gridRapports.getChildren()) {
                    if (node instanceof Button && ((Button) node).getText().equals("✔ Sélectionner")) {
                        node.setStyle("-fx-background-color: #E0E0E0;"); // Griser les autres boutons
                    }
                }
                btnSelectionner.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Marquer le bouton sélectionné
            });
            gridRapports.add(btnSelectionner, 6, row);



            // ✅ Ajouter le bouton "Ouvrir"
            Button btnOuvrir = new Button("📂 Ouvrir");
            String cheminFichier = rapport.getRessources().isEmpty() ? "" : rapport.getRessources().get(0);
            btnOuvrir.setOnAction(event -> ouvrirFichier(cheminFichier));
            gridRapports.add(btnOuvrir, 6, row);

            row++;
        }
    }


    @FXML
    private void selectionnerFichier() {
        if (rapportSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un rapport avant de choisir un fichier.");
            return;
        }
        System.out.println("📌 Rapport sélectionné : " + rapportSelectionne.getLibelleR());
    }


    @FXML
    private void supprimerRapport() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer le rapport '" + rapportSelectionne.getLibelleR() + "' ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                rapportService.delete(rapportSelectionne);
                showAlert("Succès", "Rapport supprimé avec succès !");
                rapportSelectionne = null;
                rafraichirListe();  // 🔄 Mettre à jour la liste
            }
        });
    }
                

    @FXML
    private void ouvrirModificationRapport() {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierrapport.fxml"));
            Parent root = loader.load();

            ModifierRapportController controller = loader.getController();
            if (controller != null) {
                controller.setRapportData(rapportSelectionne);
                System.out.println("📌 Rapport transmis : " + rapportSelectionne.getLibelleR());
            } else {
                System.out.println("❌ Erreur : Le contrôleur ModifierRapportController est NULL !");
            }

            Stage stage = new Stage();
            stage.setTitle("Modifier un Rapport");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface de modification.");
            e.printStackTrace();
        }
    }





    @FXML
    private void ouvrirAjoutRapport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ajoutrapport.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Rapport");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout.");
        }
    }


    // ✅ Méthode utilitaire pour afficher une alerte
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private VBox sidebar;
    @FXML
    private void retourMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mainGUI.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void filtrerRapportsParMission() {
        String selectedMission = cbMissions.getValue(); // Récupérer la mission sélectionnée

        if (selectedMission == null || selectedMission.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner une mission !");
            return;
        }

        ObservableList<Rapport> filteredRapports = FXCollections.observableArrayList();

        for (Rapport rapport : rapportList) {
            if (rapport.getMission() != null && rapport.getMission().getNomMission() != null) {
                if (rapport.getMission().getNomMission().equals(selectedMission)) {
                    filteredRapports.add(rapport);
                }
            }
        }

        if (filteredRapports.isEmpty()) {
            showAlert("Information", "Aucun rapport trouvé pour cette mission.");
        }

        afficherRapports(filteredRapports);
    }

    @FXML
    private void afficherTousLesRapports() {
        afficherRapports(rapportList); // Recharge toute la liste
    }


}
