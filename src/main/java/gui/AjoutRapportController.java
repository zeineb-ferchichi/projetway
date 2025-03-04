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

    @FXML
    private void initialize() {
        System.out.println("🚀 Initialisation de AjoutRapportController !");
        chargerMissions();
    }

    private void chargerMissions() {
        List<Mission> missions = missionService.getAll();
        for (Mission mission : missions) {
            missionNames.add(mission.getNomMission());
        }
        cbMission.setItems(missionNames);
    }

    @FXML
    private void ajouterRapport() {
        String nomRapport = TFNomRapport.getText().trim();
        LocalDate dateExp = DPDateExp.getValue();
        String missionName = cbMission.getValue();

        if (nomRapport.isEmpty() || dateExp == null || missionName == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        Mission mission = missionService.getMissionByName(missionName);
        Rapport nouveauRapport = new Rapport(nomRapport, dateExp, ressources, mission);
        rapportService.add(nouveauRapport);

        showAlert("Succès", "Rapport ajouté avec succès !");

        // 🔄 Fermer la fenêtre et rafraîchir automatiquement la liste des rapports
        fermerFenetre();
        rafraichirListeRapports();
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

    private void rafraichirListeRapports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/affrapport.fxml"));
            Parent root = loader.load();
            AfficherRapportController controller = loader.getController();
            controller.rafraichirListe();
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
