package gui;

import entities.Mission;
import entities.StatutTermint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.MissionService;
import java.io.IOException;
import java.util.List;

public class AfficherMission {
    private MainGUIController mainGUIController;
    public static AfficherMission afficherMissionsInstance;

    @FXML
    private TextField searchMissionField;
    private ObservableList<Mission> missionList = FXCollections.observableArrayList();

    private final MissionService missionService = new MissionService();

    @FXML
    private GridPane gridMissions;

    public void setMainController(MainGUIController mainGUIController) {
        this.mainGUIController = mainGUIController;
    }
    @FXML
    private void initialize() {
        missionService.mettreAJourStatuts(); // Vérifier et mettre à jour les statuts des missions
        loadMissions(); // Charger les missions après mise à jour
    }



    @FXML
    private void filtrerMissions() {
        String searchText = searchMissionField.getText().toLowerCase();
        ObservableList<Mission> filteredList = FXCollections.observableArrayList();

        for (Mission mission : missionList) {
            if (mission.getNomMission().toLowerCase().contains(searchText) ){

                filteredList.add(mission);
            }
        }

        afficherMissions(filteredList);
    }
    private void afficherMissions(ObservableList<Mission> missions) {
        gridMissions.getChildren().clear();
        int rowIndex = 1;

        for (Mission mission : missions) {
            gridMissions.add(new Label(String.valueOf(mission.getIdMission())), 0, rowIndex);
            gridMissions.add(new Label(mission.getNomMission()), 1, rowIndex);
            gridMissions.add(new Label(mission.getDate_deb().toString()), 2, rowIndex);
            gridMissions.add(new Label(mission.getDate_fin().toString()), 3, rowIndex);
            gridMissions.add(new Label(mission.getdescription()), 4, rowIndex);
            gridMissions.add(new Label(mission.getStatut().toString()), 5, rowIndex);

            Button btnModifier = new Button("📝 Modifier");
            btnModifier.setOnAction(event -> modifierMission(mission));
            btnModifier.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");
            gridMissions.add(btnModifier, 6, rowIndex);

            Button btnSupprimer = new Button("🗑 Supprimer");
            btnSupprimer.setOnAction(event -> supprimerMission(mission));
            btnSupprimer.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");
            gridMissions.add(btnSupprimer, 7, rowIndex);

            rowIndex++;
        }
    }


    private void loadMissions() {
            gridMissions.getChildren().clear(); // Nettoyer avant d'afficher les nouvelles missions

            List<Mission> missions = missionService.getAll();
            int row = 1;

            for (Mission mission : missions) {
                gridMissions.add(new Label(String.valueOf(mission.getIdMission())), 0, row);
                gridMissions.add(new Label(mission.getNomMission()), 1, row);
                gridMissions.add(new Label(mission.getDate_deb().toString()), 2, row);
                gridMissions.add(new Label(mission.getDate_fin().toString()), 3, row);
                gridMissions.add(new Label(mission.getdescription()), 4, row);
                gridMissions.add(new Label(mission.getStatut().toString()), 5, row);

                // ✅ Bouton Modifier (Bleu)
                // ✅ Bouton Modifier (Bleu)
                Button modifyButton = new Button("📝 Modifier");
                modifyButton.getStyleClass().add("button-modifier");
                modifyButton.setOnAction(event -> ouvrirFenetreModification(mission));
                gridMissions.add(modifyButton, 6, row);
                modifyButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-font-weight: bold;");

                gridMissions.getChildren().clear();
                missionList.setAll(missionService.getAll());

                afficherMissions(missionList);
// ✅ Bouton Supprimer (Rouge)
                Button deleteButton = new Button("🗑 ");
                deleteButton.getStyleClass().add("button-supprimer");
                deleteButton.setOnAction(event -> supprimerMission(mission));
                gridMissions.add(deleteButton, 7, row);
                deleteButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-weight: bold;");



                row++;
            }

    }

    private void ouvrirFenetreModification(Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierMission.fxml"));
            Parent root = loader.load();

            ModifierMission controller = loader.getController();
            controller.setMissionData(mission);  // Assure-toi que cette méthode est bien appelée

            Stage stage = new Stage();
            stage.setTitle("Modifier Mission");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadMissions(); // Rafraîchir la liste après modification
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }

    private void supprimerMission(Mission mission) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la mission ?");
        alert.setContentText("Voulez-vous vraiment supprimer cette mission ?");

        // Ajouter les boutons OK et Annuler
        ButtonType buttonTypeOui = new ButtonType("Oui");
        ButtonType buttonTypeNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);

        // Attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeOui) {
                missionService.delete(mission);
                showAlert("Succès", "✅ Mission supprimée avec succès !");
                rafraichirListeMissions(); // ✅ Rafraîchir la liste après suppression
            }
        });
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void refreshMissions() {
        loadMissions();  // 🔄 Recharge les missions affichées
        System.out.println("🔄 Rafraîchissement des missions...");
    }
    @FXML
    private void ouvrirAjoutMission() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/addmission.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Mission");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre d'ajout !");
        }
    }


    public void rafraichirListeMissions() {
        gridMissions.getChildren().clear(); // Nettoyer avant d'afficher les nouvelles missions

        List<Mission> missions = missionService.getAll();
        int rowIndex = 1;

        for (Mission mission : missions) {
            gridMissions.add(new Label(String.valueOf(mission.getIdMission())), 0, rowIndex);
            gridMissions.add(new Label(mission.getNomMission()), 1, rowIndex);
            gridMissions.add(new Label(mission.getDate_deb().toString()), 2, rowIndex);
            gridMissions.add(new Label(mission.getDate_fin().toString()), 3, rowIndex);
            gridMissions.add(new Label(mission.getdescription()), 4, rowIndex);
            gridMissions.add(new Label(mission.getStatut().toString()), 5, rowIndex);

            // ✅ Bouton Modifier avec style
            Button btnModifier = new Button("📝 Modifier");
            btnModifier.setOnAction(event -> modifierMission(mission));
            btnModifier.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-font-weight: bold;");

            // ✅ Bouton Supprimer avec style
            Button btnSupprimer = new Button("🗑 Supprimer");
            btnSupprimer.setOnAction(event -> supprimerMission(mission));
            btnSupprimer.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-weight: bold;");

            // Ajouter les boutons dans la table
            gridMissions.add(btnModifier, 6, rowIndex);
            gridMissions.add(btnSupprimer, 7, rowIndex);

            rowIndex++;
        }

    }


    @FXML
    private void ouvrirAccueil() {
        changerVue("/views/mainGUI.fxml");
    }

    @FXML
    private void ouvrirGestionMissions() {
        changerVue("/views/affmission.fxml");
    }

    @FXML
    private void ouvrirGestionRapports() {
        changerVue("/views/affrapport.fxml");
    }

    @FXML
    private void quitterApplication() {
        System.exit(0);
    }

    private void changerVue(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue: " + fxml);
        }
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
    private Button btnBack;

    private void modifierMission(Mission mission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierMission.fxml"));
            Parent root = loader.load();

            ModifierMission controller = loader.getController();
            controller.setMission(mission);  // Vérifiez que setMission() existe dans ModifierMission.java

            Stage stage = new Stage();
            stage.setTitle("Modifier Mission");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadMissions(); // Rafraîchir après modification
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
