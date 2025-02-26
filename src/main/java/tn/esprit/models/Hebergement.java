package tn.esprit.models;


public class Hebergement {

    private int id;
    private String nom;
    private String type;
    private String adresse;
    private String ville;
    private String pays;
    private int capacite;
    private int prix;

    public Hebergement(){}
    public Hebergement(int id, String nom, String type, String adresse, String ville, String pays, int capacite, int prix) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.adresse = adresse;
        this.ville = ville;
        this.pays = pays;
        this.capacite = capacite;
        this.prix = prix;
    }


    public Hebergement(String nom, String type, String adresse, String ville, String pays, int capacite, int prix) {
        this.nom = nom;
        this.type = type;
        this.adresse = adresse;
        this.ville = ville;
        this.pays = pays;
        this.capacite = capacite;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ville='" + ville + '\'' +
                ", pays='" + pays + '\'' +
                ", capacite=" + capacite +
                ", prix=" + prix +
                '}';
    }
}