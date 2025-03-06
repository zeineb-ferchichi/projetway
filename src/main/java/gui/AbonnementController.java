package gui;
import services.PDFExportAbonnementService;

import javafx.collections.FXCollections;
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
import util.WindowsNotificationUtil; // ✅ Notifications Windows

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AbonnementController {

    @FXML private ComboBox<String> comboType;
    @FXML private TextField txtMontant, txtDureeValable, txtTransportId, searchAbonnement;
    @FXML private ComboBox<String> comboStatus;
    @FXML private ComboBox<String> comboSortBy;
    @FXML private VBox abonDisplay;

    private final AbonnementService service = new AbonnementService();
    private Abonnement selectedAbonnement = null;

    @FXML
    public void initialize() {
        System.out.println("Chargement du contrôleur AbonnementController...");

        if (comboType == null || abonDisplay == null) {
            System.out.println("⚠️ Erreur: Vérifiez que les IDs des composants FXML correspondent bien aux attributs de la classe !");
            return;
        }

        loadComboBoxes();
        loadAbonnements();
        initializeSortComboBox();
    }

    private void loadComboBoxes() {
        if (comboType != null) {
            comboType.setItems(FXCollections.observableArrayList("Mensuel", "Annuel", "Hebdomadaire"));
            comboType.setValue("Mensuel");
        }

        if (comboStatus != null) {
            comboStatus.setItems(FXCollections.observableArrayList("Actif", "Expiré", "Suspendu"));
            comboStatus.setValue("Actif");
        }
    }

    private void initializeSortComboBox() {
        if (comboSortBy != null) {
            comboSortBy.setItems(FXCollections.observableArrayList("Type", "Montant", "Durée", "Statut"));
            comboSortBy.setOnAction(event -> sortAbonnements());
        }
    }

    private void loadAbonnements() {
        abonDisplay.getChildren().clear();
        List<Abonnement> abonnements = service.getAll();
        for (Abonnement abo : abonnements) {
            abonDisplay.getChildren().add(createAbonnementCard(abo));
        }
    }

    @FXML
    private void filterAbonnements() {
        if (searchAbonnement.getText() == null) return;
        String keyword = searchAbonnement.getText().toLowerCase().trim();

        List<Abonnement> abonnements = service.getAll().stream()
                .filter(a -> a.getType_abonnem().toLowerCase().contains(keyword) ||
                        String.valueOf(a.getMontant()).contains(keyword) ||
                        String.valueOf(a.getDuree_valable()).contains(keyword) ||
                        String.valueOf(a.getTransport_id()).contains(keyword) ||
                        a.getStatus_abonnem().toLowerCase().contains(keyword))
                .toList();

        abonDisplay.getChildren().clear();
        for (Abonnement abo : abonnements) {
            abonDisplay.getChildren().add(createAbonnementCard(abo));
        }
    }

    @FXML
    private void sortAbonnements() {
        String sortBy = comboSortBy.getValue();
        if (sortBy == null) return;

        List<Abonnement> abonnements = service.getAll();
        Comparator<Abonnement> comparator = switch (sortBy) {
            case "Type" -> Comparator.comparing(Abonnement::getType_abonnem);
            case "Montant" -> Comparator.comparingDouble(Abonnement::getMontant);
            case "Durée" -> Comparator.comparingInt(Abonnement::getDuree_valable);
            case "Statut" -> Comparator.comparing(Abonnement::getStatus_abonnem);
            default -> null;
        };

        if (comparator != null) {
            abonnements.sort(comparator);
        }

        abonDisplay.getChildren().clear();
        for (Abonnement abo : abonnements) {
            abonDisplay.getChildren().add(createAbonnementCard(abo));
        }
    }

    private HBox createAbonnementCard(Abonnement abo) {
        HBox card = new HBox(10);
        card.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-radius: 5; -fx-border-width: 1;");

        Text info = new Text(
                "ID Abonnement: " + abo.getId_abonnem() + // ✅ Affichage avec ID
                        " | Type: " + abo.getType_abonnem() +
                        " | Montant: " + abo.getMontant() +
                        " | Durée: " + abo.getDuree_valable() + " jours" +
                        " | Statut: " + abo.getStatus_abonnem() +
                        " | Transport ID: " + abo.getTransport_id()
        );

        Button btnDelete = new Button("supprimer❌");
        btnDelete.setOnAction(e -> {
            selectedAbonnement = abo;
            deleteAbonnement();
        });

        Button btnEdit = new Button("modifier✏️");
        btnEdit.setOnAction(e -> selectAbonnementForEdit(abo));

        card.getChildren().addAll(info, btnEdit, btnDelete);
        return card;
    }

    @FXML
    private void deleteAbonnement() {
        if (selectedAbonnement == null) {
            showAlert("Erreur", "⚠ Veuillez sélectionner un abonnement à supprimer!", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'abonnement ?");
        alert.setContentText("Voulez-vous vraiment supprimer cet abonnement ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int abonnementId = selectedAbonnement.getId_abonnem(); // ✅ Récupération de l'ID
            service.delete(abonnementId);
            loadAbonnements();
            showAlert("Succès", "✅ Abonnement supprimé avec succès!", Alert.AlertType.INFORMATION);

            // 🔔 Notification avec ID
            WindowsNotificationUtil.showWindowsNotification("Suppression Abonnement", "L'abonnement avec l'ID " + abonnementId + " a été supprimé !");
            selectedAbonnement = null;
        }
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
    private void selectAbonnementForEdit(Abonnement abo) {
        if (abo == null) return;

        selectedAbonnement = abo;
        comboType.setValue(abo.getType_abonnem());
        txtMontant.setText(String.valueOf(abo.getMontant()));
        txtDureeValable.setText(String.valueOf(abo.getDuree_valable()));
        txtTransportId.setText(String.valueOf(abo.getTransport_id()));
        comboStatus.setValue(abo.getStatus_abonnem());
    }

    @FXML
    private void addAbonnement() {
        if (!validateFields()) {
            return;
        }

        Abonnement abo = new Abonnement(
                comboType.getValue(),
                Double.parseDouble(txtMontant.getText()),
                Integer.parseInt(txtDureeValable.getText()),
                Integer.parseInt(txtTransportId.getText()),
                comboStatus.getValue()
        );

        service.add(abo);  // ❌ ERREUR : la méthode add() retourne void !

        loadAbonnements();
        clearFields();
        showAlert("Succès", "✅ Abonnement ajouté avec succès!", Alert.AlertType.INFORMATION);

        WindowsNotificationUtil.showWindowsNotification("Ajout Abonnement", "L'abonnement " + abo.getType_abonnem() + " a été ajouté !");
    }


    @FXML
    private void updateAbonnement() {
        if (selectedAbonnement == null) {
            showAlert("Erreur", "⚠ Veuillez sélectionner un abonnement à modifier !", Alert.AlertType.ERROR);
            return;
        }

        if (!validateFields()) {
            return;
        }

        selectedAbonnement.setType_abonnem(comboType.getValue());
        selectedAbonnement.setMontant(Double.parseDouble(txtMontant.getText()));
        selectedAbonnement.setDuree_valable(Integer.parseInt(txtDureeValable.getText()));
        selectedAbonnement.setTransport_id(Integer.parseInt(txtTransportId.getText()));
        selectedAbonnement.setStatus_abonnem(comboStatus.getValue());

        service.update(selectedAbonnement);
        loadAbonnements();
        clearFields();
        showAlert("Succès", "✅ Abonnement modifié avec succès!", Alert.AlertType.INFORMATION);

        // 🔔 Notification avec ID
        WindowsNotificationUtil.showWindowsNotification("Modification Abonnement", "L'abonnement avec l'ID " + selectedAbonnement.getId_abonnem() + " a été modifié !");
        selectedAbonnement = null;
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
    /**
     * Vérifie que tous les champs requis sont remplis.
     * @return true si tous les champs sont remplis, sinon false.
     */
    private boolean validateFields() {
        if (txtMontant.getText().isEmpty() || txtDureeValable.getText().isEmpty() ||
                txtTransportId.getText().isEmpty() || comboType.getValue() == null ||
                comboStatus.getValue() == null) {

            showAlert("Erreur", "⚠ Veuillez remplir tous les champs obligatoires.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Double.parseDouble(txtMontant.getText());
            Integer.parseInt(txtDureeValable.getText());
            Integer.parseInt(txtTransportId.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "⚠ Les champs Montant, Durée Valable et Transport ID doivent être des nombres valides.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    @FXML
    private void exportAbonnementsPDF() {
        String filePath = System.getProperty("user.home") + "/Desktop/Abonnements.pdf";
        PDFExportAbonnementService.exportAbonnementsToPDF(service.getAll(), filePath);

        showAlert("Export PDF", "Le fichier PDF a été généré sur le Bureau.", Alert.AlertType.INFORMATION);
    }



}
