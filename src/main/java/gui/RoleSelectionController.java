package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoleSelectionController {

    @FXML
    void handleAdminClick(ActionEvent event) {
        loadRoleScene("/AfficherForum.fxml", true,event);
    }

    @FXML
    void handleEmployeeClick(ActionEvent event) {
        loadRoleScene("/AfficherForum.fxml", false,event);
    }

    private void loadRoleScene(String fxmlPath, boolean isAdmin, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            AfficherForum controller = loader.getController();
            controller.setIsAdmin(isAdmin);
            System.out.println(isAdmin);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // ✅ Correct way to close the current stage
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
