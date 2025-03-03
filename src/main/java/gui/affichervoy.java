package gui;
import com.sun.glass.ui.Menu;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import entities.voyage;
import servies.voyageservice;
import java.util.List;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class affichervoy {@FXML
private ListView<HBox> voyageListView;

    private final voyageservice voyageService = new voyageservice();

    @FXML
    public void initialize() {
        loadVoyages();


    }

    private void loadVoyages() {
        List<voyage> voyages = voyageService.getAll();
        ObservableList<HBox> items = FXCollections.observableArrayList();

        for (voyage v : voyages) {
            items.add(createVoyageBox(v));
        }

        voyageListView.setItems(items);
    }

    private HBox createVoyageBox(voyage v) {
        Label label = new Label(v.getDestination() + " | " + v.getDate_depart() + " -> " + v.getDate_retour());
        Button modifyBtn = new Button("Modifier");
        Button deleteBtn = new Button("Supprimer");

        modifyBtn.setOnAction(e -> modifyVoyage(v));
        deleteBtn.setOnAction(e -> deleteVoyage(v));

        HBox hbox = new HBox(10, label, modifyBtn, deleteBtn);
        hbox.setAlignment(Pos.CENTER_LEFT);

        return hbox;
    }

    private void modifyVoyage(voyage v) {
        Stage stage = new Stage();
        VBox vbox = new VBox(10);




        // DatePickers for departure and return dates
        DatePicker departDatePicker = new DatePicker(v.getDate_depart());
        DatePicker retourDatePicker = new DatePicker(v.getDate_retour());

        Button saveButton = new Button("Enregistrer");

        saveButton.setOnAction(e -> {

            v.setDate_depart(departDatePicker.getValue());
            v.setDate_retour(retourDatePicker.getValue());
            voyageService.update(v);
            loadVoyages();
            stage.close();
        });

        vbox.getChildren().addAll(

                new Label("Date de départ"), departDatePicker,
                new Label("Date de retour"), retourDatePicker,
                saveButton
        );

        Scene scene = new Scene(vbox, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void deleteVoyage(voyage v) {
        voyageService.delete(v.getIdvoyage());
        loadVoyages(); // Refresh ListView
    }
}
