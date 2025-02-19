package gui;

import entities.trajet;
import entities.voyage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import servies.trajetservice;
import servies.voyageservice;
import javafx.scene.control.ButtonType;
import gui.modifiervoyage;


import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import static javax.swing.JOptionPane.showInputDialog;

public class AfficherTables implements Initializable {

    @FXML
    private ListView<String> listViewVoyages;

    @FXML
    private ListView<String> listViewTrajets;

    private final voyageservice voyageService = new voyageservice();
    private final trajetservice trajetService = new trajetservice();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewVoyages.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listViewTrajets.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadVoyages();
        loadTrajets();
    }

    private void loadVoyages() {
        List<voyage> voyages = voyageService.getAll();
        ObservableList<String> observableVoyages = FXCollections.observableArrayList();

        for (voyage v : voyages) {
            observableVoyages.add("Destination: " + v.getDestination() + ", Départ: " + v.getDate_depart() + ", Retour: " + v.getDate_retour());
        }
        listViewVoyages.setItems(observableVoyages);
    }

    private void loadTrajets() {
        List<trajet> trajets = trajetService.getAll();
        ObservableList<String> observableTrajets = FXCollections.observableArrayList();

        for (trajet t : trajets) {
            observableTrajets.add("Voyage ID: " + t.getIdvoyage() + ", Transport: " + t.getType_transport() + ", Compagnie: " + t.getCompagnie());
        }
        listViewTrajets.setItems(observableTrajets);
    }

    @FXML
    private void actualiser() {
        loadVoyages();
        loadTrajets();
    }

    @FXML
    private void supprimerVoyage() {
        int selectedIndex = listViewVoyages.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert(AlertType.WARNING, "Aucun voyage sélectionné", "Veuillez sélectionner un voyage à supprimer.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText(null);
        confirm.setContentText("Voulez-vous vraiment supprimer ce voyage ?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<voyage> voyages = voyageService.getAll();
            voyage voyageToDelete = voyages.get(selectedIndex);
            voyageService.delete(voyageToDelete.getIdvoyage());
            loadVoyages();
            showAlert(AlertType.INFORMATION, "Succès", "Le voyage a bien été supprimé.");
        }
    }
    @FXML
    private void modifierVoyage() {
        int selectedIndex = listViewVoyages.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert(Alert.AlertType.WARNING, "Aucun voyage sélectionné", "Veuillez sélectionner un voyage à modifier.");
            return;
        }

        // Retrieve selected voyage
        List<voyage> voyages = voyageService.getAll();
        voyage voyageToModify = voyages.get(selectedIndex);

        int voyageId = voyageToModify.getIdvoyage();
        System.out.println("🔹 Selected Voyage ID: " + voyageId);

        // Get new values from user input
        String newDestination = showInputDialog("Nouvelle Destination", "Entrez la nouvelle destination:", voyageToModify.getDestination());
        String newDepartureDate = showInputDialog("Nouvelle Date de Départ", "Entrez la nouvelle date de départ:", voyageToModify.getDate_depart());
        String newDateRetour = showInputDialog("Nouvelle Date de Retour", "Entrez la nouvelle date de retour:", voyageToModify.getDate_retour());

        // Ensure values are not null
        if (newDestination == null || newDepartureDate == null || newDateRetour == null) {
            showAlert(Alert.AlertType.WARNING, "Modification annulée", "Tous les champs doivent être remplis.");
            return;
        }

        // Debugging
        System.out.println("✅ New values for voyage:");
        System.out.println(" - Destination: " + newDestination);
        System.out.println(" - Date Depart: " + newDepartureDate);
        System.out.println(" - Date Retour: " + newDateRetour);

        // Call update function
        boolean success = modifiervoyage.updateVoyage(
                voyageId,
                newDestination,
                newDepartureDate,
                newDateRetour
        );

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le voyage a été modifié avec succès.");
            loadVoyages(); // Refresh list
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "❌ Échec de la modification du voyage.");
        }
    }




    private String showInputDialog(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue); // Set default value
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null); // Return user input or null if cancelled
    }


    @FXML
    private void supprimerTrajet() {
        int selectedIndex = listViewTrajets.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert(AlertType.WARNING, "Aucun trajet sélectionné", "Veuillez sélectionner un trajet à supprimer.");
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText(null);
        confirm.setContentText("Voulez-vous vraiment supprimer ce trajet ?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<trajet> trajets = trajetService.getAll();
            trajet trajetToDelete = trajets.get(selectedIndex);
            trajetService.delete(trajetToDelete.getTrajet_id());
            loadTrajets();
            showAlert(AlertType.INFORMATION, "Succès", "Le trajet a bien été supprimé.");
        }
    }

    @FXML
    private void modifierTrajet() {
        int selectedIndex = listViewTrajets.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert(AlertType.WARNING, "Aucun trajet sélectionné", "Veuillez sélectionner un trajet à modifier.");
            return;
        }

        // Retrieve selected trajet
        List<trajet> trajets = trajetService.getAll();
        trajet trajetToModify = trajets.get(selectedIndex);

        int trajetId = trajetToModify.getTrajet_id();
        System.out.println("🔹 Selected Trajet ID: " + trajetId);

        // Call update function
        boolean success = modifiertrajet.updateTrajet(
                trajetId,
                trajetToModify.getIdvoyage(),
                showInputDialog("Type de Transport", "Entrez le type de transport:", trajetToModify.getType_transport()),
                showInputDialog("Compagnie", "Entrez le nom de la compagnie:", trajetToModify.getCompagnie()),
                showInputDialog("Numéro", "Entrez le numéro du trajet:", trajetToModify.getNumero()),
                showInputDialog("Date de Départ", "Entrez la nouvelle date de départ:", trajetToModify.getDate_depart()),
                showInputDialog("Date d'Arrivée", "Entrez la nouvelle date d'arrivée:", trajetToModify.getDate_arrivee()),
                showInputDialog("Ville de Départ", "Entrez la nouvelle ville de départ:", trajetToModify.getVille_depart()),
                showInputDialog("Ville d'Arrivée", "Entrez la nouvelle ville d'arrivée:", trajetToModify.getVille_arrivee()),
                Double.parseDouble(showInputDialog("Coût", "Entrez le coût du trajet:", String.valueOf(trajetToModify.getCout())))
        );

        if (success) {
            showAlert(AlertType.INFORMATION, "Succès", "Le trajet a été modifié avec succès.");
            loadTrajets();
        } else {
            showAlert(AlertType.ERROR, "Erreur", "❌ Échec de la modification du trajet.");
        }
    }



    @FXML
    private void retour() {
        // Implémenter la logique pour retourner à la fenêtre précédente
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
