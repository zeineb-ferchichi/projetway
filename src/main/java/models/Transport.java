package models

public class Transport {
    // Attributs correspondant aux colonnes de la table `transport`
    private int id_transp;
    private String type_transp;
    private String nom_station;  // ✅ Remplace date_transp par nom_station
    private String zone_geographique;
    private Integer note;
    // Constructeur sans paramètres
    public Transport() {}

    // Constructeur avec paramètres incluant l'ID (utilisé pour récupérer un transport existant)
    public Transport(int id_transp, String type_transp, String nom_station, String zone_geographique, Integer note) {
        this.id_transp = id_transp;
        this.type_transp = type_transp;
        this.nom_station = nom_station;
        this.zone_geographique = zone_geographique;
        this.note = note;
    }
    public Transport(int id_transp, String type_transp, String nom_station, String zone_geographique) {
        this.id_transp = id_transp;
        this.type_transp = type_transp;
        this.nom_station = nom_station;
        this.zone_geographique = zone_geographique;
    }

    // Constructeur sans l'ID (généralement pour l'insertion)
    public Transport(String type_transp, String nom_station, String zone_geographique) {
        this.type_transp = type_transp;
        this.nom_station = nom_station;
        this.zone_geographique = zone_geographique;
    }

    // Getters et Setters pour chaque attribut
    public int getId_transp() {
        return id_transp;
    }

    public void setId_transp(int id_transp) {
        this.id_transp = id_transp;
    }

    public String getType_transp() {
        return type_transp;
    }

    public void setType_transp(String type_transp) {
        this.type_transp = type_transp;
    }

    public String getNom_station() {
        return nom_station;
    }

    public void setNom_station(String nom_station) {
        this.nom_station = nom_station;
    }

    public String getZone_geographique() {
        return zone_geographique;
    }

    public void setZone_geographique(String zone_geographique) {
        this.zone_geographique = zone_geographique;
    }

    public Integer getNote() {
        return note;
    }

    // Méthode toString() pour afficher les informations de l'objet
    @Override
    public String toString() {
        return "Transport{" +
                "id_transp=" + id_transp +
                ", type_transp='" + type_transp + '\'' +
                ", nom_station='" + nom_station + '\'' +
                ", zone_geographique='" + zone_geographique + '\'' +
                '}';
    }
}
