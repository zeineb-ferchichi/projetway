package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Abonnement;
import services.AbonnementService;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class AbonnementController {

    @FXML private TextField txtType, txtMontant, txtTransportId;
    @FXML private DatePicker dateDebut, dateFin;
    @FXML private VBox abonDisplay;

    private final AbonnementService service = new AbonnementService();
    private Abonnement selectedAbonnement = null;

    @FXML
    public void initialize() {
        loadAbonnements();
    }

    /**
     * Charge et affiche tous les abonnements.
     */
    private void loadAbonnements() {
        abonDisplay.getChildren().clear();
        List<Abonnement> abonnements = service.getAll();
        for (Abonnement abo : abonnements) {
            abonDisplay.getChildren().add(createAbonnementCard(abo));
        }
    }

    @FXML
    private void addAbonnement() {
        if (validateFields()) {
            try {
                Abonnement abo = new Abonnement(
                        txtType.getText(),
                        Double.parseDouble(txtMontant.getText()),
                        Date.valueOf(dateDebut.getValue()),
                        Date.valueOf(dateFin.getValue()),
                        Integer.parseInt(txtTransportId.getText())
                );
                service.add(abo);
                loadAbonnements();
                clearFields();
                showAlert("Succès", "Abonnement ajouté avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Vérifiez les valeurs saisies !", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateAbonnement() {
        if (selectedAbonnement == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonnement à modifier!", Alert.AlertType.ERROR);
            return;
        }
        if (validateFields()) {
            try {
                selectedAbonnement.setType_abonnem(txtType.getText());
                selectedAbonnement.setMontant(Double.parseDouble(txtMontant.getText()));
                selectedAbonnement.setDate_debut(Date.valueOf(dateDebut.getValue()));
                selectedAbonnement.setDate_fin(Date.valueOf(dateFin.getValue()));
                selectedAbonnement.setTransport_id(Integer.parseInt(txtTransportId.getText()));

                service.update(selectedAbonnement);
                loadAbonnements();
                clearFields();
                showAlert("Succès", "Abonnement modifié avec succès!", Alert.AlertType.INFORMATION);
                selectedAbonnement = null;
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de modifier l'abonnement!", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void deleteAbonnement() {
        if (selectedAbonnement == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonnement à supprimer!", Alert.AlertType.ERROR);
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'abonnement ?");
        alert.setContentText("Voulez-vous vraiment supprimer cet abonnement ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.delete(selectedAbonnement);
            loadAbonnements();
            showAlert("Succès", "Abonnement supprimé avec succès!", Alert.AlertType.INFORMATION);
            selectedAbonnement = null;
        }
    }

    @FXML
    private void clearFields() {
        txtType.clear();
        txtMontant.clear();
        txtTransportId.clear();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        selectedAbonnement = null;
    }

    /**
     * Vérifie si tous les champs sont remplis.
     */
    private boolean validateFields() {
        if (txtType.getText().isEmpty() || txtMontant.getText().isEmpty() ||
                dateDebut.getValue() == null || dateFin.getValue() == null ||
                txtTransportId.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    /**
     * Affiche une alerte avec un message personnalisé.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Retour à la page d'accueil.
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) abonDisplay.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page d'accueil!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Sélectionne un abonnement pour modification.
     */
    private void selectAbonnementForEdit(Abonnement abo) {
        selectedAbonnement = abo;
        txtType.setText(abo.getType_abonnem());
        txtMontant.setText(String.valueOf(abo.getMontant()));

        if (abo.getDate_debut() != null) {
            Instant instantDebut = abo.getDate_debut().toInstant();
            LocalDate localDateDebut = instantDebut.atZone(ZoneId.systemDefault()).toLocalDate();
            dateDebut.setValue(localDateDebut);
        }

        if (abo.getDate_fin() != null) {
            Instant instantFin = abo.getDate_fin().toInstant();
            LocalDate localDateFin = instantFin.atZone(ZoneId.systemDefault()).toLocalDate();
            dateFin.setValue(localDateFin);
        }

        txtTransportId.setText(String.valueOf(abo.getTransport_id()));
    }

    /**
     * Crée une "carte" pour afficher un abonnement avec un bouton de suppression et de modification.
     */
    private HBox createAbonnementCard(Abonnement abo) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10; -fx-background-color: #BBDEFB; -fx-border-color: #0D47A1; -fx-border-radius: 5; -fx-border-width: 2;");

        Text info = new Text(
                "Type: " + abo.getType_abonnem() +
                        " | Montant: " + abo.getMontant() +
                        " | Début: " + abo.getDate_debut() +
                        " | Fin: " + abo.getDate_fin() +
                        " | Transport ID: " + abo.getTransport_id()
        );

        Button btnDelete = new Button("❌");
        btnDelete.setOnAction(e -> {
            selectedAbonnement = abo;
            deleteAbonnement();
        });

        Button btnEdit = new Button("✏️");
        btnEdit.setOnAction(e -> selectAbonnementForEdit(abo));

        card.getChildren().addAll(info, btnEdit, btnDelete);
        return card;
    }
}
