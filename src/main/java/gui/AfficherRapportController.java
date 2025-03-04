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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AfficherRapportController {
    @FXML
    private GridPane gridRapports;
    @FXML
    private TextField searchRapportField;
    @FXML
    private ComboBox<String> cbMissions;
    @FXML
    private Button btnAjouterRapport, btnSupprimerRapport, btnModifierRapport, btnRafraichir;
    @FXML
    private VBox sidebar;

    private final RapportService rapportService = new RapportService();
    private final MissionService missionService = new MissionService();
    private final ObservableList<Rapport> rapportList = FXCollections.observableArrayList();
    private Rapport rapportSelectionne;

    @FXML
    private void initialize() {
        System.out.println("📌 Initialisation terminée !");
        chargerMissions();
        rafraichirListe(); // 🔄 Assure-toi que cette méthode est bien appelée
    }


    private void chargerMissions() {
        List<Mission> missions = missionService.getAll();
        ObservableList<String> missionNames = FXCollections.observableArrayList();
        for (Mission mission : missions) {
            missionNames.add(mission.getNomMission());
        }
        cbMissions.setItems(missionNames);
    }

    @FXML
    private void rafraichirListe() {
        gridRapports.getChildren().clear(); // 🔄 Supprime tout avant de recharger
        List<Rapport> rapports = rapportService.getAll();

        if (rapports.isEmpty()) {
            System.out.println("❌ Aucun rapport trouvé !");
            return;
        }

        int row = 1;
        for (Rapport rapport : rapports) {
            Label lblId = new Label(String.valueOf(rapport.getIdRapport()));
            Label lblNom = new Label(rapport.getLibelleR());
            Label lblDate = new Label(rapport.getDateExpo().toString());
            Label lblMission = new Label(rapport.getMission().getNomMission());

            gridRapports.add(lblId, 0, row);
            gridRapports.add(lblNom, 1, row);
            gridRapports.add(lblDate, 2, row);
            gridRapports.add(lblMission, 3, row);

            Button btnModifier = new Button("✏ Modifier");
            btnModifier.setOnAction(event -> ouvrirModificationRapport(rapport));
            btnModifier.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white;");
            gridRapports.add(btnModifier, 4, row);

            row++;
        }
    }
    @FXML
    private void ouvrirGestionMissions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/affmission.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quitterApplication() {
        Stage stage = (Stage) sidebar.getScene().getWindow();
        stage.close();
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


    private void ouvrirModificationRapport(Rapport rapport) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierRapport.fxml"));
            Parent root = loader.load();

            ModifierRapportController controller = loader.getController();
            if (controller != null) {
                controller.setRapportData(rapport);
                System.out.println("📌 Rapport transmis : " + rapport.getLibelleR());
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
    private void supprimerRapport(Rapport rapport) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer ce rapport ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                rapportService.delete(rapport);
                showAlert("Succès", "Rapport supprimé !");
                rafraichirListe();  // Rafraîchir la liste après suppression
            }
        });
    }


    @FXML
    private void filtrerRapportsParMission() {
        String selectedMission = cbMissions.getValue();
        if (selectedMission == null || selectedMission.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner une mission !");
            return;
        }

        ObservableList<Rapport> filteredRapports = FXCollections.observableArrayList();
        for (Rapport rapport : rapportList) {
            if (rapport.getMission() != null && rapport.getMission().getNomMission().equalsIgnoreCase(selectedMission.trim())) {
                filteredRapports.add(rapport);
            }
        }

        if (filteredRapports.isEmpty()) {
            showAlert("Information", "Aucun rapport trouvé pour cette mission.");
        } else {
            afficherRapports(filteredRapports);
        }
    }

    private void afficherRapports(ObservableList<Rapport> rapports) {
        gridRapports.getChildren().clear();
        int rowIndex = 1;

        for (Rapport rapport : rapports) {
            gridRapports.add(new Label(String.valueOf(rapport.getIdRapport())), 0, rowIndex);
            gridRapports.add(new Label(rapport.getLibelleR()), 1, rowIndex);
            gridRapports.add(new Label(rapport.getDateExpo().toString()), 2, rowIndex);

            // Bouton Modifier
            Button btnModifier = new Button("📝 Modifier");
            btnModifier.setOnAction(event -> ouvrirModificationRapport(rapport));
            btnModifier.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");
            gridRapports.add(btnModifier, 3, rowIndex);

            // Bouton Supprimer
            Button btnSupprimer = new Button("🗑 Supprimer");
            btnSupprimer.setOnAction(event -> supprimerRapport(rapport));
            btnSupprimer.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");
            gridRapports.add(btnSupprimer, 5, rowIndex);
            btnSupprimer.setManaged(true);
            btnSupprimer.setVisible(true);

            rowIndex++;
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

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
