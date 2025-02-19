package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Hebergement;
import tn.esprit.services.HebergementService;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifierHebergement implements Initializable {

    @FXML private TextField txtNom;
    @FXML private TextField txtType;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtVille;
    @FXML private TextField txtPays;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtPrix;
    @FXML private Button btnModifier;

    private Hebergement hebergement;
    private final HebergementService hebergementService = new HebergementService();

    /**
     * Initialise les champs avec les données de l'hébergement sélectionné.
     */
    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;

        if (hebergement != null) {
            txtNom.setText(hebergement.getNom());
            txtType.setText(hebergement.getType());
            txtAdresse.setText(hebergement.getAdresse());
            txtVille.setText(hebergement.getVille());
            txtPays.setText(hebergement.getPays());
            txtCapacite.setText(String.valueOf(hebergement.getCapacite()));
            txtPrix.setText(String.valueOf(hebergement.getPrix()));
        }
    }

    @FXML
    private void updateHebergement() {
        if (hebergement != null) {
            // Mettre à jour l'objet avec les nouvelles valeurs saisies
            hebergement.setNom(txtNom.getText());
            hebergement.setType(txtType.getText());
            hebergement.setAdresse(txtAdresse.getText());
            hebergement.setVille(txtVille.getText());
            hebergement.setPays(txtPays.getText());
            hebergement.setCapacite(Integer.parseInt(txtCapacite.getText()));
            hebergement.setPrix(Integer.parseInt(txtPrix.getText()));

            // Appeler le service pour mettre à jour la base de données
            hebergementService.update(hebergement);

            // Fermer la fenêtre après modification
            Stage stage = (Stage) btnModifier.getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Rien à initialiser ici, les données seront chargées via setHebergement()
    }
}
