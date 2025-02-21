package models;

import java.util.Date;

public class Abonnement {
    private int id_abonnem;
    private String type_abonnem;
    private double montant;
    private Date date_debut;
    private Date date_fin;
    private int transport_id;

    // ✅ Constructeur par défaut
    public Abonnement() {}

    // ✅ Constructeur avec ID
    public Abonnement(int id_abonnem) {
        this.id_abonnem = id_abonnem;
    }

    // ✅ Constructeur complet avec tous les champs
    public Abonnement(int id_abonnem, String type_abonnem, double montant, Date date_debut, Date date_fin, int transport_id) {
        this.id_abonnem = id_abonnem;
        this.type_abonnem = type_abonnem;
        this.montant = montant;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.transport_id = transport_id;
    }

    // ✅ Constructeur sans ID (pour insertion en base de données)
    public Abonnement(String type_abonnem, double montant, Date date_debut, Date date_fin, int transport_id) {
        this.type_abonnem = type_abonnem;
        this.montant = montant;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.transport_id = transport_id;
    }

    // ✅ Getters et Setters (CORRIGÉS)
    public int getId_abonnem() {
        return id_abonnem;
    }

    public void setId_abonnem(int id_abonnem) {
        this.id_abonnem = id_abonnem;
    }

    public String getType_abonnem() {
        return type_abonnem;
    }

    public void setType_abonnem(String type_abonnem) {
        this.type_abonnem = type_abonnem;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public int getTransport_id() {
        return transport_id;
    }

    public void setTransport_id(int transport_id) {
        this.transport_id = transport_id;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id_abonnem=" + id_abonnem +
                ", type_abonnem='" + type_abonnem + '\'' +
                ", montant=" + montant +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", transport_id=" + transport_id +
                '}';
    }
}
