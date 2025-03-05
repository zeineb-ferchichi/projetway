package entities;

import java.time.LocalDate;
import java.util.List;

public class voyage {

    private int idvoyage;
    private Destination destination;
    private LocalDate date_depart;
    private LocalDate date_retour;
    private List<trajet> trajets;  // Liste des trajets associés à ce voyage

    public voyage(String destination, String dateDepart, String dateRetour) {
        // Ce constructeur pourrait être utilisé pour une conversion ou une initialisation
        // mais tu devras probablement le remplir de façon appropriée.
    }

    public voyage(int idvoyage, Destination destination, LocalDate date_depart, LocalDate date_retour) {
        validateDates(date_depart, date_retour);
        this.idvoyage = idvoyage;
        this.destination = destination;
        this.date_depart = date_depart;
        this.date_retour = date_retour;
    }

    public voyage(Destination destination, LocalDate date_depart, LocalDate date_retour) {
        validateDates(date_depart, date_retour);
        this.destination = destination;
        this.date_depart = date_depart;
        this.date_retour = date_retour;
    }

    public voyage() {
    }

    private void validateDates(LocalDate date_depart, LocalDate date_retour) {
        if (date_depart == null || date_retour == null) {
            throw new IllegalArgumentException("Les dates ne peuvent pas être nulles.");
        }
        if (!date_depart.isBefore(date_retour)) {
            throw new IllegalArgumentException("La date de départ doit être avant la date de retour.");
        }
    }

    @Override
    public String toString() {
        return "Voyage{" +
                "id=" + idvoyage +
                ", destination=" + destination +
                ", date_depart=" + date_depart +
                ", date_retour=" + date_retour +
                '}';
    }

    public int getIdvoyage() {
        return idvoyage;
    }

    public void setIdvoyage(int idvoyage) {
        this.idvoyage = idvoyage;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public LocalDate getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(LocalDate date_depart) {
        validateDates(date_depart, this.date_retour);
        this.date_depart = date_depart;
    }

    public LocalDate getDate_retour() {
        return date_retour;
    }

    public void setDate_retour(LocalDate date_retour) {
        validateDates(this.date_depart, date_retour);
        this.date_retour = date_retour;
    }

    public List<trajet> getTrajets() {
        return trajets;  // Retourne la liste des trajets associés au voyage
    }

    public void setTrajets(List<trajet> trajets) {
        this.trajets = trajets;  // Assigne la liste des trajets
    }

    public enum Destination {
        FRANCE, USA, CANADA, GERMANY, ITALY, SPAIN, UK, JAPAN, CHINA, BRAZIL, AUSTRALIA, INDIA;

        public String toLowerCase() {
            return this.name().toLowerCase();
        }
    }
}
