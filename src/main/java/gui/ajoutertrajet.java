package gui;

import entities.trajet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import servies.trajetservice;

import java.io.IOException;

public class ajoutertrajet {

    private final trajetservice trajetService = new trajetservice();

    @FXML
    private TextField TFIdVoyage;

    @FXML
    private TextField TFTypeTransport;

    @FXML
    private TextField TFCompagnie;

    @FXML
    private TextField TFNumero;

    @FXML
    private TextField TFDateDepart;

    @FXML
    private TextField TFDateArrivee;

    @FXML
    private TextField TFVilleDepart;

    @FXML
    private TextField TFVilleArrivee;

    @FXML
    private TextField TFCout;

    @FXML
    void ajouter1(ActionEvent event) {
        try {
            int idVoyage = Integer.parseInt(TFIdVoyage.getText());
            String typeTransport = TFTypeTransport.getText();
            String compagnie = TFCompagnie.getText();
            String numero = TFNumero.getText();
            String dateDepart = TFDateDepart.getText();
            String dateArrivee = TFDateArrivee.getText();
            String villeDepart = TFVilleDepart.getText();
            String villeArrivee = TFVilleArrivee.getText();
            double cout = Double.parseDouble(TFCout.getText());

            trajet trajet = new trajet(idVoyage, typeTransport, compagnie, numero, dateDepart, dateArrivee, villeDepart, villeArrivee, cout);
            trajetService.add(trajet);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Trajet ajouté avec succès!");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer des valeurs valides.");
            alert.showAndWait();
        }
    }

    @FXML
    void afficher1(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/affichertrajet.fxml"));
            TFIdVoyage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'affichage des trajets: " + e.getMessage());
        }
    }
}
