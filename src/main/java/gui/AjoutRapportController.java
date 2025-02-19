
package gui;

import entities.Mission;
import entities.Rapport;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.MissionService;
import services.RapportService;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AjoutRapportController {

    @FXML
    private TextField TFLibelle;
    @FXML
    private DatePicker DPDateExpo;
    @FXML
    private ListView<String> LVFiles;
    @FXML
    private ComboBox<Mission> cbMission;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnAnnuler;

    private final MissionService missionService = new MissionService();
    private final RapportService rapportService = new RapportService();
    private final List<String> selectedFiles = new ArrayList<>();

    @FXML
    private void initialize() {
        // Charger les missions dans la ComboBox
        cbMission.setItems(FXCollections.observableArrayList(missionService.getAll()));
    }

    @FXML
    private void addFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier");
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            for (File file : files) {
                selectedFiles.add(file.getAbsolutePath());
                LVFiles.getItems().add(file.getName());
            }
        }
    }

    @FXML
    private void ajouterRapport() {
        String libelle = TFLibelle.getText();
        LocalDate dateExpo = DPDateExpo.getValue();
        Mission selectedMission = cbMission.getValue();

        if (libelle.isEmpty() || dateExpo == null || selectedFiles.isEmpty() || selectedMission == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // Création et enregistrement du rapport
        Rapport rapport = new Rapport(libelle, dateExpo, selectedFiles, selectedMission);
        rapportService.add(rapport);
        showAlert("Succès", "✅ Rapport ajouté avec succès !");

        // Fermer la fenêtre après ajout
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void annuler() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
