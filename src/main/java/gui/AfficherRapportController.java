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
        rafraichirListe();
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
    public void rafraichirListe() {
        if (gridRapports == null) {
            System.out.println("⚠ gridRapports est NULL !");
            return;
        }

        gridRapports.getChildren().clear();
        rapportList.setAll(rapportService.getAll());

        int row = 1;
        for (Rapport rapport : rapportList) {
            gridRapports.add(new Label(String.valueOf(rapport.getIdRapport())), 0, row);
            gridRapports.add(new Label(rapport.getLibelleR()), 1, row);
            gridRapports.add(new Label(rapport.getDateExpo().toString()), 2, row);

            Button btnModifier = new Button("📝 Modifier");
            btnModifier.setOnAction(event -> ouvrirModificationRapport(rapport));
            btnModifier.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");
            gridRapports.add(btnModifier, 3, row);

            Button btnSupprimer = new Button("🗑 Supprimer");
            btnSupprimer.setOnAction(event -> supprimerRapport(rapport));
            btnSupprimer.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");
            gridRapports.add(btnSupprimer, 4, row);

            row++;
        }
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

            Button btnModifier = new Button("📝 Modifier");
            btnModifier.setOnAction(event -> ouvrirModificationRapport(rapport));
            btnModifier.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");
            gridRapports.add(btnModifier, 3, rowIndex);

            Button btnSupprimer = new Button("🗑 Supprimer");
            btnSupprimer.setOnAction(event -> supprimerRapport(rapport));
            btnSupprimer.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");
            gridRapports.add(btnSupprimer, 4, rowIndex);

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
