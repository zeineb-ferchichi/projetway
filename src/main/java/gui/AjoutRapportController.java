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
import javafx.stage.Stage;
import services.MissionService;
import services.RapportService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AjoutRapportController {
    @FXML
    private TextField TFNomRapport;
    @FXML
    private DatePicker DPDateExp;
    @FXML
    private ListView<String> LVFiles;
    @FXML
    private ComboBox<String> cbMission;
    @FXML
    private Button btnAjouter;

    private final RapportService rapportService = new RapportService();
    private final MissionService missionService = new MissionService();
    private final ObservableList<String> missionNames = FXCollections.observableArrayList();
    private final List<String> ressources = new ArrayList<>();

    private AfficherRapportController parentController; // 🔄 Ajout du contrôleur parent

    public void setParentController(AfficherRapportController parentController) {
        this.parentController = parentController;
    }
    @FXML
    private void initialize() {
        List<Mission> allMissions = missionService.getAll();
        ObservableList<String> missionNames = FXCollections.observableArrayList();

        for (Mission mission : allMissions) {
            System.out.println("✅ Mission trouvée : " + mission.getNomMission() + " - Statut : " + mission.getStatut());

            // Vérifier le statut correctement
            if (mission.getStatut() != null && mission.getStatut().toString().equalsIgnoreCase("TERMINE")) {
                missionNames.add(mission.getNomMission());
            }
        }

        if (missionNames.isEmpty()) {
            showAlert("Information", "Aucune mission terminée disponible pour ajouter un rapport.");
        } else {
            cbMission.setItems(missionNames);
        }
    }



    @FXML
    private void ajouterRapport() {
        String nomRapport = TFNomRapport.getText().trim();
        LocalDate dateExp = DPDateExp.getValue();
        String selectedMissionName = cbMission.getValue();

        if (nomRapport.isEmpty() || dateExp == null || selectedMissionName == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Vérifier si la mission est terminée
        Mission selectedMission = missionService.getMissionByName(selectedMissionName);
        if (!selectedMission.getStatut().toString().equalsIgnoreCase("Terminée")) { // ✅ Correction
            showAlert("Erreur", "Impossible d'ajouter un rapport à une mission non terminée !");
            return;
        }

        // Création du rapport
        List<String> fichiers = new ArrayList<>(LVFiles.getItems()); // Liste des fichiers
        Rapport nouveauRapport = new Rapport(nomRapport, dateExp, fichiers, selectedMission);

        // Ajout en base de données
        rapportService.add(nouveauRapport);
        showAlert("Succès", "Rapport ajouté avec succès !");

        // Rafraîchir la liste des rapports et fermer la fenêtre
        fermerFenetre();
        if (parentController != null) {
            parentController.rafraichirListe(); // ✅ Rafraîchissement correct
        }
    }

    @FXML
    private void addFile() {
        // Ajoute un fichier fictif pour tester
        ressources.add("fichier_test.pdf");
        LVFiles.getItems().add("fichier_test.pdf");
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
