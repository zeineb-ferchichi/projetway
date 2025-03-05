package gui;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import models.Transport;

public class RatingController {
    @FXML
    public Slider ratingSlider;
    private Transport selectedTransport;
    private RatingCallback callback;
    public void setTransport(Transport transport, RatingCallback callback) {
        this.selectedTransport = transport;
        this.callback = callback;
    }
    @FXML
    private void confirmRating() {
        double rating = ratingSlider.getValue();

        if (callback != null) {
            callback.onRatingSubmitted(selectedTransport, (int) rating);
        }

        // Close the window
        Stage stage = (Stage) ratingSlider.getScene().getWindow();
        stage.close();
    }
}
