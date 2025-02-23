package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.List;
import java.util.Optional;

public class AbonnementController {

    @FXML private ComboBox<String> comboType;
    @FXML private TextField txtMontant, txtDureeValable, txtTransportId, searchAbonnement;
    @FXML private ComboBox<String> comboStatus;
    @FXML private VBox abonDisplay;

    private final AbonnementService service = new AbonnementService(); // ✅ Ajout de la déclaration
    private Abonnement selectedAbonnement = null;

    @FXML
    public void initialize() {
        loadComboBoxes();
        loadAbonnements();
    }

    private void loadComboBoxes() {
        comboType.getItems().addAll("Mensuel", "Annuel", "Hebdomadaire");
        comboType.setValue("Mensuel");

        comboStatus.getItems().addAll("Actif", "Expiré", "Suspendu");
        comboStatus.setValue("Actif");
    }

    private void loadAbonnements() {
        abonDisplay.getChildren().clear();
        List<Abonnement> abonnements = service.getAll();
        for (Abonnement abo : abonnements) {
            abonDisplay.getChildren().add(createAbonnementCard(abo));
        }
    }

    // Méthode pour filtrer les abonnements
    @FXML
    private void filterAbonnements() {
        String keyword = searchAbonnement.getText().toLowerCase().trim();
        ObservableList<Abonnement> filteredList = FXCollections.observableArrayList();

        for (Abonnement abo : service.getAll()) {
            if (abo.getType_abonnem().toLowerCase().contains(keyword) ||
                    String.valueOf(abo.getMontant()).contains(keyword) ||
                    String.valueOf(abo.getDuree_valable()).contains(keyword) ||
                    String.valueOf(abo.getTransport_id()).contains(keyword) ||
                    abo.getStatus_abonnem().toLowerCase().contains(keyword)) {
                filteredList.add(abo);
            }
        }

        abonDisplay.getChildren().clear();
        for (Abonnement abo : filteredList) {
            abonDisplay.getChildren().add(createAbonnementCard(abo));
        }
    }

    private HBox createAbonnementCard(Abonnement abo) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-radius: 5; -fx-border-width: 1;");

        Text info = new Text(
                "Type: " + abo.getType_abonnem() +
                        " | Montant: " + abo.getMontant() +
                        " | Durée: " + abo.getDuree_valable() + " jours" +
                        " | Statut: " + abo.getStatus_abonnem() +
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

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) abonDisplay.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à la page d'accueil!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void addAbonnement() {
        if (validateFields()) {
            try {
                Abonnement abo = new Abonnement(
                        comboType.getValue(),
                        Double.parseDouble(txtMontant.getText()),
                        Integer.parseInt(txtDureeValable.getText()),
                        Integer.parseInt(txtTransportId.getText()),
                        comboStatus.getValue()
                );
                service.add(abo);
                loadAbonnements();
                clearFields();
                showAlert("Succès", "✅ Abonnement ajouté avec succès!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Vérifiez les valeurs saisies!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Veuillez remplir tous les champs !", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateAbonnement() {
        if (selectedAbonnement == null) {
            showAlert("Erreur", "Veuillez sélectionner un abonnement à modifier!", Alert.AlertType.ERROR);
            return;
        }

        if (validateFields()) {
            selectedAbonnement.setType_abonnem(comboType.getValue());
            selectedAbonnement.setMontant(Double.parseDouble(txtMontant.getText()));
            selectedAbonnement.setDuree_valable(Integer.parseInt(txtDureeValable.getText()));
            selectedAbonnement.setTransport_id(Integer.parseInt(txtTransportId.getText()));
            selectedAbonnement.setStatus_abonnem(comboStatus.getValue());

            service.update(selectedAbonnement);
            loadAbonnements();
            clearFields();
            showAlert("Succès", "✅ Abonnement modifié avec succès!", Alert.AlertType.INFORMATION);
            selectedAbonnement = null;
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
            service.delete(selectedAbonnement.getId_abonnem());
            loadAbonnements();
            showAlert("Succès", "✅ Abonnement supprimé avec succès!", Alert.AlertType.INFORMATION);
            selectedAbonnement = null;
        }
    }

    @FXML
    private void clearFields() {
        comboType.setValue("Mensuel");
        txtMontant.clear();
        txtDureeValable.clear();
        txtTransportId.clear();
        comboStatus.setValue("Actif");
        selectedAbonnement = null;
    }

    private boolean validateFields() {
        return !txtMontant.getText().isEmpty() &&
                !txtDureeValable.getText().isEmpty() &&
                !txtTransportId.getText().isEmpty();
    }

    private void selectAbonnementForEdit(Abonnement abo) {
        selectedAbonnement = abo;
        comboType.setValue(abo.getType_abonnem());
        txtMontant.setText(String.valueOf(abo.getMontant()));
        txtDureeValable.setText(String.valueOf(abo.getDuree_valable()));
        txtTransportId.setText(String.valueOf(abo.getTransport_id()));
        comboStatus.setValue(abo.getStatus_abonnem());
    }
}
