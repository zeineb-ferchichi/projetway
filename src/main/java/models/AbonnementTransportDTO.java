package models;

import java.time.LocalDate;

public class AbonnementTransportDTO {
    // Champs pour l'abonnement
    private int idAbonnem;
    private String typeAbonnem;
    private double montant;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    // Champs pour le transport
    private int idTransp;
    private String typeTransport;

    // Constructeur par défaut
    public AbonnementTransportDTO() {}

    // Constructeur avec paramètres
    public AbonnementTransportDTO(int idAbonnem, String typeAbonnem, double montant, LocalDate dateDebut, LocalDate dateFin,
                                  int idTransp, String typeTransport) {
        this.idAbonnem = idAbonnem;
        this.typeAbonnem = typeAbonnem;
        this.montant = montant;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idTransp = idTransp;
        this.typeTransport = typeTransport;
    }

    // Getters et Setters
    public int getIdAbonnem() {
        return idAbonnem;
    }

    public void setIdAbonnem(int idAbonnem) {
        this.idAbonnem = idAbonnem;
    }

    public String getTypeAbonnem() {
        return typeAbonnem;
    }

    public void setTypeAbonnem(String typeAbonnem) {
        this.typeAbonnem = typeAbonnem;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getIdTransp() {
        return idTransp;
    }

    public void setIdTransp(int idTransp) {
        this.idTransp = idTransp;
    }

    public String getTypeTransport() {
        return typeTransport;
    }

    public void setTypeTransport(String typeTransport) {
        this.typeTransport = typeTransport;
    }

    @Override
    public String toString() {
        return "AbonnementTransportDTO{" +
                "idAbonnem=" + idAbonnem +
                ", typeAbonnem='" + typeAbonnem + '\'' +
                ", montant=" + montant +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", idTransp=" + idTransp +
                ", typeTransport='" + typeTransport + '\'' +
                '}';
    }
}
