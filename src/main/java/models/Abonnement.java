package models;

public class Abonnement {
    private int id_abonnem;
    private String type_abonnem;
    private double montant;
    private int duree_valable; // 🔹 Remplace date_debut et date_fin (ex: 30 jours)
    private int transport_id;
    private String statut_abonnement; // 🔹 Ajout d'un statut : "Actif" ou "Expiré"

    public Abonnement(int id_abonnem, String type_abonnem, double montant, int duree_valable, int transport_id, String statut_abonnement) {
        this.id_abonnem = id_abonnem;
        this.type_abonnem = type_abonnem;
        this.montant = montant;
        this.duree_valable = duree_valable;
        this.transport_id = transport_id;
        this.statut_abonnement = statut_abonnement;
    }

    // 🔹 Getters et Setters
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

    public int getDuree_valable() {
        return duree_valable;
    }

    public void setDuree_valable(int duree_valable) {
        this.duree_valable = duree_valable;
    }

    public int getTransport_id() {
        return transport_id;
    }

    public void setTransport_id(int transport_id) {
        this.transport_id = transport_id;
    }

    public String getStatut_abonnement() {
        return statut_abonnement;
    }

    public void setStatut_abonnement(String statut_abonnement) {
        this.statut_abonnement = statut_abonnement;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id_abonnem=" + id_abonnem +
                ", type_abonnem='" + type_abonnem + '\'' +
                ", montant=" + montant +
                ", duree_valable=" + duree_valable + " jours" +
                ", transport_id=" + transport_id +
                ", statut_abonnement='" + statut_abonnement + '\'' +
                '}';
    }
}
