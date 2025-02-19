package tn.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.util.DBConnection;

import java.io.IOException;

public class TestFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Make sure to initialize the DB connection before loading the FXML
        DBConnection.getInstance();  // This will initialize the connection

        // Load the FXML and show the scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

