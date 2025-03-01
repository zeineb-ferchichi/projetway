
package gui;

import entities.Mission;
import entities.Rapport;
import entities.StatutTermint;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.MissionService;
import services.RapportService;
import util.InputValidation;
import javafx.scene.input.KeyEvent; // ✅ Correct

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
    private List<String> ressources;

    @FXML
    private void initialize() {
        List<Mission> allMissions = missionService.getAll();
        List<Mission> missionsTerminees = new ArrayList<>();

        for (Mission mission : allMissions) {
            if (mission.getStatut() == StatutTermint.TERMINE) {
                missionsTerminees.add(mission);
            }
        }

        cbMission.setItems(FXCollections.observableArrayList(missionsTerminees));
    }
@FXML

    private void addFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier");
        List<File> files = fileChooser.showOpenMultipleDialog(null);

        if (files != null) {
            for (File file : files) {
                selectedFiles.add(file.getAbsolutePath());
            }
        }
    }

    @FXML
    private void ajouterRapport() {
        String libelle = TFLibelle.getText().trim();
        Mission missionAssociee = cbMission.getSelectionModel().getSelectedItem();

        if (libelle.isEmpty() || missionAssociee == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        LocalDate dateExpo = LocalDate.now(); // ✅ Prend la date du jour

        // ✅ Vérifier si la liste `selectedFiles` est vide avant d'appeler `String.join()`
        String ressources = selectedFiles.isEmpty() ? "" : String.join(", ", selectedFiles);

        Rapport nouveauRapport = new Rapport(libelle, dateExpo, Collections.singletonList(ressources), missionAssociee);
        rapportService.add(nouveauRapport);

        showAlert("Succès", "✅ Rapport ajouté avec succès !");
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
    @FXML
    private void validerNomRapport(KeyEvent event) {
        String text = TFLibelle.getText();

        if (!text.matches("^[a-zA-ZÀ-ÿ\\s]*$")) {
            event.consume(); // Empêche l'ajout de caractères non valides
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
            e.printStackTrace();
        }
    }



}
