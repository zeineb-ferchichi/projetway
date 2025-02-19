package gui;

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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.RapportService;
import gui.ModifierRapportController;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AfficherRapportController {
    @FXML
    private GridPane gridRapports;

    private Rapport rapportSelectionne;
    private final RapportService rapportService = new RapportService();

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
    private void initialize() {
        rafraichirListe();
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


    private void ouvrirFichier(String chemin) {
        File fichier = new File(chemin);
        if (fichier.exists()) {
            try {
                Desktop.getDesktop().open(fichier);
            } catch (IOException e) {
                showAlert("Erreur", "Impossible d'ouvrir le fichier !");
            }
        } else {
            showAlert("Erreur", "Fichier introuvable !");
        }
    }

    private Rapport obtenirRapportSelectionne() {

        if (rapportList.isEmpty()) {
            return null;
        }
        return rapportList.get(0); // ⚠️ À adapter si vous avez une sélection précise
    }

    @FXML
    private void supprimerRapport() {
        if (rapportSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un rapport à supprimer !");
            return;
        }

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
        if (rapportSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un rapport à modifier !");
            return;
        }

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
}
