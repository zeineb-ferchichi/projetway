package tn.esprit.models;

import java.util.Date;

public class Reservation {
    private int id;
    private String clientName;
    private Date dateDebut;
    private Date dateFin;
    private int hebergementId;

    // ✅ Ajout d'un constructeur sans arguments
    public Reservation() {
    }

    public Reservation(int id, String clientName, Date dateDebut, Date dateFin, int hebergementId) {
        this.id = id;
        this.clientName = clientName;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.hebergementId = hebergementId;
    }

    public Reservation(String clientName, Date dateDebut, Date dateFin, int hebergementId) {
        this.clientName = clientName;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.hebergementId = hebergementId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getHebergementId() {
        return hebergementId;
    }

    public void setHebergementId(int hebergementId) {
        this.hebergementId = hebergementId;
    }

    @Override
    public String toString() {
        return "Reservation{id=" + id + ", clientName='" + clientName + "', dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", hebergementId=" + hebergementId + "}";
    }
}
