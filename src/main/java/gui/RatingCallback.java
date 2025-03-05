package gui;


import models.Transport;

public interface RatingCallback {
    void onRatingSubmitted(Transport transport, int rating);
}
