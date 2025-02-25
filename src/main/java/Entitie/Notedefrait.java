package Entitie;

public class Notedefrait {

    private int Id = 0; // Valeur par défaut de 0
    private String Nomactivite;
    private String Description;
    private String Lienfacture;
    private int userId; // ID de l'employé associé

    // Constructeur avec ID
    public Notedefrait(int id, String nomActivite, String description, String lienFacture, int userId) {
        this.Id = id;
        this.Nomactivite = nomActivite;
        this.Description = description;
        this.Lienfacture = lienFacture;
        this.userId = userId;
    }

    // Constructeur sans ID (Id prendra la valeur par défaut de 0)
    public Notedefrait(String nomActivite, String description, String lienFacture, int userId) {
        this.Nomactivite = nomActivite;
        this.Description = description;
        this.Lienfacture = lienFacture;
        this.userId = userId;
        // Pas besoin d'initialiser Id, il est déjà à 0 par défaut
    }

    // Getters et Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNomactivite() {
        return Nomactivite;
    }

    public void setNomactivite(String Nomactivite) {
        this.Nomactivite = Nomactivite;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getLienfacture() {
        return Lienfacture;
    }

    public void setLienfacture(String Lienfacture) {
        this.Lienfacture = Lienfacture;
    }

    // Redéfinition de toString pour afficher les infos facilement
    @Override
    public String toString() {
        return "Note de frais{" +
                "ID=" + Id +
                ", Activité='" + Nomactivite + '\'' +
                ", Description='" + Description + '\'' +
                ", Facture='" + Lienfacture + '\'' +
                ", Employé ID=" + userId +
                '}';
    }
}
