package entities;

public class trajet {

    int trajet_id;
    int idvoyage;
    String type_transport;
    String compagnie;
    String numero;
    String date_depart;
    String date_arrivee;
    String ville_depart;
    String ville_arrivee;
    double cout;

    // Constructeur par défaut
    public trajet() {
    }

    // Constructeur avec tous les attributs
    public trajet(int trajet_id, int idvoyage, String type_transport, String compagnie, String numero, String date_depart, String date_arrivee, String ville_depart, String ville_arrivee, double cout) {
        this.trajet_id = trajet_id;
        this.idvoyage = idvoyage;
        this.type_transport = type_transport;
        this.compagnie = compagnie;
        this.numero = numero;
        this.date_depart = date_depart;
        this.date_arrivee = date_arrivee;
        this.ville_depart = ville_depart;
        this.ville_arrivee = ville_arrivee;
        this.cout = cout;
    }

    // Constructeur sans l'ID (utile pour l'insertion, car l'ID est auto-généré)
    public trajet(int idvoyage, String type_transport, String compagnie, String numero, String date_depart, String date_arrivee, String ville_depart, String ville_arrivee, double cout) {
        this.idvoyage = idvoyage;
        this.type_transport = type_transport;
        this.compagnie = compagnie;
        this.numero = numero;
        this.date_depart = date_depart;
        this.date_arrivee = date_arrivee;
        this.ville_depart = ville_depart;
        this.ville_arrivee = ville_arrivee;
        this.cout = cout;
    }

    // Getters et Setters

    public int getTrajet_id() {
        return trajet_id;
    }

    public void setTrajet_id(int trajet_id) {
        this.trajet_id = trajet_id;
    }

    public int getIdvoyage() {
        return idvoyage;
    }

    public void setIdvoyage(int idvoyage) {
        this.idvoyage = idvoyage;
    }

    public String getType_transport() {
        return type_transport;
    }

    public void setType_transport(String type_transport) {
        this.type_transport = type_transport;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(String date_depart) {
        this.date_depart = date_depart;
    }

    public String getDate_arrivee() {
        return date_arrivee;
    }

    public void setDate_arrivee(String date_arrivee) {
        this.date_arrivee = date_arrivee;
    }

    public String getVille_depart() {
        return ville_depart;
    }

    public void setVille_depart(String ville_depart) {
        this.ville_depart = ville_depart;
    }

    public String getVille_arrivee() {
        return ville_arrivee;
    }

    public void setVille_arrivee(String ville_arrivee) {
        this.ville_arrivee = ville_arrivee;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }
}
