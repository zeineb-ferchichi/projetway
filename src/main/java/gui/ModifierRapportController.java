package gui;

import entities.Rapport;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.RapportService;
import java.sql.Date;
import java.time.LocalDate;
import java.io.File;
import java.util.ArrayList;

public class ModifierRapportController {

    // ✅ Référence au rapport sélectionné

    // ✅ Injection des champs du formulaire
    @FXML
    private TextField TFNomRapport;

    @FXML
    private DatePicker DPDateCreation;

    @FXML
    private DatePicker DPDateExp;

    @FXML
    private TextField TFFichier;
    private Rapport rapport;

    private RapportService rapportService = new RapportService();

    private void remplirChamps() {
        if (rapport != null) {
            TFNomRapport.setText(rapport.getLibelleR());
            DPDateExp.setValue(rapport.getDateExpo());
        }
    }

    // ✅ Méthode pour choisir un fichier
    @FXML
    private void choisirFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier rapport");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            TFFichier.setText(selectedFile.getAbsolutePath());
        }
    }

    // ✅ Méthode pour mettre à jour le rapport
    @FXML
    private void updateRapport() {
        if (rapport == null) {
            showAlert("Erreur", "Aucun rapport sélectionné !");
            return;
        }

        String nom = TFNomRapport.getText().trim();
        String fichierJoint = TFFichier.getText().trim();

        if (nom.isEmpty()) {
            showAlert("Erreur", "Veuillez saisir un nom de rapport !");
            return;
        }

        rapport.setLibelleR(nom);
        rapport.setDateExpo(LocalDate.now()); // ✅ Mettre automatiquement la date d'exposition à aujourd'hui

        if (!fichierJoint.isEmpty()) {
            if (!(rapport.getRessources() instanceof ArrayList)) {
                rapport.setRessources(new ArrayList<>(rapport.getRessources()));
            }
            if (!(rapport.getRessources() instanceof java.util.ArrayList)) {
                rapport.setRessources(new ArrayList<>(rapport.getRessources())); // Convertir en liste modifiable
            }
            rapport.getRessources().clear();
            rapport.getRessources().add(fichierJoint);
        }

        rapportService.update(rapport);
        showAlert("Succès", "Rapport mis à jour avec succès !");

        // 🔄 Fermer la fenêtre après mise à jour
        Stage stage = (Stage) TFFichier.getScene().getWindow();
        stage.close();
    }




    // ✅ Méthode d'affichage d'une alerte
    private void showAlert(String titre, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setRapportData(Rapport rapport) {
        this.rapport = rapport;
        remplirChamps(); // Remplit les champs avec les données du rapport sélectionné
    }

    @FXML
    private void initialize() {
        System.out.println("✅ Initialisation de ModifierRapportController...");

        if (TFNomRapport == null) {
            System.out.println("❌ TFNomRapport est NULL !");
        }
        if (DPDateCreation == null) {
            System.out.println("❌ DPDateCreation est NULL !");
        }
        if (TFFichier == null) {
            System.out.println("❌ TFFichier est NULL !");
        }
        if (DPDateCreation == null) {
            System.out.println("❌ DPDateCreation est NULL ! Vérifiez votre FXML.");
        } else {
            DPDateCreation.setValue(LocalDate.now());
        }

    }

    public void setRapport(Rapport rapport) {
        this.rapport = rapport;

        if (rapport != null) {
            if (TFNomRapport != null) {
                TFNomRapport.setText(rapport.getLibelleR());
            }

            if (DPDateCreation != null) {
                DPDateCreation.setValue(rapport.getDateExpo() != null ? rapport.getDateExpo() : LocalDate.now());
            }


            if (TFFichier != null) {
                TFFichier.setText(rapport.getRessources() != null && !rapport.getRessources().isEmpty()
                        ? String.join(", ", rapport.getRessources())
                        : "");
            }
        }
    }



}



