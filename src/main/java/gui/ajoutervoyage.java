package gui;

import entities.voyage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import servies.voyageservice;

import java.io.IOException;

public class ajoutervoyage {

    private final voyageservice voyageservice = new voyageservice();
    @FXML
    private TextField TFAge;

    @FXML
    private TextField TFNom;

    @FXML
    private TextField TFPrenom;

    @FXML
    void ajouter(ActionEvent event) {
        String destination = TFPrenom.getText();
        String date_depart= TFNom.getText();
        String date_retour = TFAge.getText();

        voyage p = new voyage(destination,date_depart,date_retour);

        voyageservice.add(p);


    }


    @FXML
    void afficher(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/affichervoyage.fxml"));
            TFAge.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }




}