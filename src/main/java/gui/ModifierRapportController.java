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
    private Rapport rapportActuel;

    // ✅ Injection des champs du formulaire
    @FXML
    private TextField TFNomRapport;

    @FXML
    private DatePicker DPDateCreation;

    @FXML
    private TextField TFFichier;

    private RapportService rapportService = new RapportService();

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
        if (TFNomRapport.getText().isEmpty() || DPDateCreation.getValue() == null || TFFichier.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        // ✅ Mettre à jour les données du rapport
        rapportActuel.setLibelleR(TFNomRapport.getText());
        rapportActuel.setDateExpo(DPDateCreation.getValue()); // Pas besoin de conversion !
        rapportActuel.setRessources(new ArrayList<>(rapportActuel.getRessources())); // 🔄 Convertit en liste modifiable
        rapportActuel.getRessources().clear();
        rapportActuel.getRessources().add(TFFichier.getText());

        // ✅ Mise à jour en base
        rapportService.update(rapportActuel);

        System.out.println("✅ Mise à jour effectuée !");
        showAlert("Succès", "Rapport mis à jour avec succès !");
        Stage stage = (Stage) TFNomRapport.getScene().getWindow();
        stage.close();  // ✅ Ferme la fenêtre de modification

// ✅ Rafraîchir la liste dans AfficherRapportController
        AfficherRapportController controller = new AfficherRapportController();
        controller.rafraichirListe();

    }

    // ✅ Méthode d'affichage d'une alerte
    private void showAlert(String titre, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void initialize() {
        System.out.println("✅ Initialisation des champs FXML...");
    }

    public void setRapportData(Rapport rapport) {
        if (TFNomRapport == null || DPDateCreation == null || TFFichier == null) {
            System.out.println("❌ Les champs FXML ne sont pas encore initialisés !");
            return;
        }

        this.rapportActuel = rapport;
        System.out.println("✅ Rapport transmis : " + rapport.getLibelleR());

        // ✅ Conversion correcte de la date
        if (rapport.getDateExpo() != null) {
            DPDateCreation.setValue(rapport.getDateExpo());
        } else {
            DPDateCreation.setValue(null);
        }

        // ✅ Ajouter le chemin du fichier joint si disponible
        TFFichier.setText(rapport.getRessources().isEmpty() ? "" : rapport.getRessources().get(0));
    }
}



