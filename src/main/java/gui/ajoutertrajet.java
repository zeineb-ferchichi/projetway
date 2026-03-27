package gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import entities.trajet;
import servies.TrajetService;
import servies.TrajetService;
import util.Datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ajoutertrajet {

    @FXML
    private ComboBox<String> TFIdVoyage; // ComboBox for voyage id
    @FXML
    private ComboBox<String> TFTypeTransport;
    @FXML
    private ComboBox<String> TFCompagnie;
    @FXML
    private ComboBox<String> TFVilleDepart;
    @FXML
    private ComboBox<String> TFVilleArrivee;
    @FXML
    private TextField TFCout;
    @FXML
    private Label lblError;

    private final TrajetService trajetService = new TrajetService();

    @FXML
    public void initialize() {
        TFTypeTransport.setItems(FXCollections.observableArrayList("Taxi", "Bus", "Avion"));
        TFCompagnie.setItems(FXCollections.observableArrayList("Air France", "Delta", "Uber", "Local Bus", "Lufthansa"));
        TFVilleDepart.setItems(FXCollections.observableArrayList("Paris", "Lyon", "Marseille", "Tokyo", "Osaka", "New York", "Los Angeles", "Chicago"));
        TFVilleArrivee.setItems(FXCollections.observableArrayList("Paris", "Lyon", "Marseille", "Tokyo", "Osaka", "New York", "Los Angeles", "Chicago"));

        populateIdVoyageComboBox();
    }

    private void populateIdVoyageComboBox() {
        List<String> idVoyageList = new ArrayList<>();

        try (Connection conn = Datasource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id_voyage FROM voyage");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                idVoyageList.add(rs.getString("id_voyage"));
            }

            TFIdVoyage.setItems(FXCollections.observableArrayList(idVoyageList));
        } catch (SQLException e) {
            lblError.setText("❌ Erreur de connexion à la base de données.");
            e.printStackTrace();
        }
    }

    @FXML
    public void ajouter1() {
        try {
            clearErrors();

            String idVoyageString = TFIdVoyage.getValue();
            String typeTransport = TFTypeTransport.getValue();
            String compagnie = TFCompagnie.getValue();
            String villeDepart = TFVilleDepart.getValue();
            String villeArrivee = TFVilleArrivee.getValue();
            String costInput = TFCout.getText();

            if (idVoyageString == null || typeTransport == null || compagnie == null ||
                    villeDepart == null || villeArrivee == null || costInput == null || costInput.trim().isEmpty()) {
                showError("⚠️ Tous les champs doivent être remplis !");
                return;
            }

            int idVoyage;
            try {
                idVoyage = Integer.parseInt(idVoyageString);
            } catch (NumberFormatException e) {
                showError("⚠️ ID Voyage doit être un nombre valide.");
                return;
            }

            double cout;
            try {
                cout = Double.parseDouble(costInput);
            } catch (NumberFormatException e) {
                showError("⚠️ Veuillez entrer un coût valide.");
                return;
            }

            if (villeDepart.equals(villeArrivee)) {
                showError("⚠️ Ville de départ et d'arrivée doivent être différentes !");
                return;
            }

            trajet newTrajet = new trajet(idVoyage, typeTransport, compagnie, cout, villeDepart, villeArrivee);
            trajetService.add(newTrajet);

            lblError.setText("✅ Trajet ajouté avec succès !");
            clearFields();

        } catch (Exception e) {
            showError("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearErrors() {
        lblError.setText("");
    }

    private void showError(String message) {
        lblError.setText(message);
    }

    private void clearFields() {
        TFIdVoyage.setValue(null);
        TFTypeTransport.setValue(null);
        TFCompagnie.setValue(null);
        TFVilleDepart.setValue(null);
        TFVilleArrivee.setValue(null);
        TFCout.clear();
    }
}
