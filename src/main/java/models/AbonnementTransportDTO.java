package models;

public class AbonnementTransportDTO {
    // Champs pour l'abonnement
    private int idAbonnem;
    private TypeAbonnement typeAbonnem; // ✅ Enum pour la liste déroulante
    private double montant;
    private int dureeValable; // ✅ Durée en jours
    private StatusAbonnement status; // ✅ Enum pour la liste déroulante

    // Champs pour le transport
    private int idTransp;
    private String typeTransport;

    // Constructeur par défaut
    public AbonnementTransportDTO() {}

    // Constructeur avec paramètres
    public AbonnementTransportDTO(int idAbonnem, TypeAbonnement typeAbonnem, double montant, int dureeValable, StatusAbonnement status,
                                  int idTransp, String typeTransport) {
        this.idAbonnem = idAbonnem;
        this.typeAbonnem = typeAbonnem;
        this.montant = montant;
        this.dureeValable = dureeValable;
        this.status = status;
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

    public TypeAbonnement getTypeAbonnem() {
        return typeAbonnem;
    }

    public void setTypeAbonnem(TypeAbonnement typeAbonnem) {
        this.typeAbonnem = typeAbonnem;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public int getDureeValable() {
        return dureeValable;
    }

    public void setDureeValable(int dureeValable) {
        this.dureeValable = dureeValable;
    }

    public StatusAbonnement getStatus() {
        return status;
    }

    public void setStatus(StatusAbonnement status) {
        this.status = status;
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
                ", typeAbonnem=" + typeAbonnem +
                ", montant=" + montant +
                ", dureeValable=" + dureeValable + " jours" +
                ", status=" + status +
                ", idTransp=" + idTransp +
                ", typeTransport='" + typeTransport + '\'' +
                '}';
    }
}
