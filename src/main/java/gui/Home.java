package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void openTransport(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Transport.fxml")); // Charge Transport.fxml
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 800, 500); // Taille uniforme
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openAbonnement(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Abonnement.fxml")); // Charge Abonnement.fxml
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 800, 500); // Taille uniforme
        stage.setScene(scene);
        stage.show();
    }
}
