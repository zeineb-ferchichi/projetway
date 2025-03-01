package entities;

import java.time.LocalDate;

public class Mission {
    private int idMission;
    private String nomMission;
    private LocalDate date_deb;
    private LocalDate date_fin;
    private String description;
    private StatutTermint statut;


    // ✅ Constructeur par défaut
    public Mission() {}

    // ✅ Constructeur avec ID (Utilisé pour DELETE et UPDATE)
    public Mission(int idMission) {
        this.idMission = idMission;
    }

    // ✅ Constructeur complet (Utilisé pour SELECT et affichage)
    public Mission(int idMission, String nomMission, LocalDate date_deb, LocalDate date_fin, String description, StatutTermint statut) {
        this.idMission = idMission;
        this.nomMission = nomMission;
        this.date_deb = date_deb;
        this.date_fin = date_fin;
        this.description = description;
        this.statut = statut;

    }
    public int getId() {
        return this.idMission;
    }

    // ✅ Constructeur avec statut (Utilisé pour INSERT avec statut initial)
    public Mission(String nomMission, LocalDate date_deb, LocalDate date_fin, String description, StatutTermint statut) {
        this.nomMission = nomMission;
        this.date_deb = date_deb;
        this.date_fin = date_fin;
        this.description = description;
        this.statut = statut;
    }


    // ✅ Constructeur sans ID (Utilisé pour INSERT)
    public Mission(String nomMission, LocalDate date_deb, LocalDate date_fin, String description) {
        this.nomMission = nomMission;
        this.date_deb = date_deb;
        this.date_fin = date_fin;
        this.description = description;
    }

    // Getters et Setters
    public int getIdMission() {
        return idMission;
    }

    public void setIdMission(int idMission) {
        this.idMission = idMission;
    }

    public String getNomMission() {
        return nomMission;
    }

    public void setNomMission(String nomMission) {
        this.nomMission = nomMission;
    }

    public LocalDate getDate_deb() {
        return date_deb;
    }
    public Mission(int idMission, String nomMission) {
        this.idMission = idMission;
        this.nomMission = nomMission;
    }

    public void setDate_deb(LocalDate date_deb) {
        this.date_deb = date_deb;
    }

    public LocalDate getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }
    public StatutTermint getStatut() {
        return statut;
    }

    public void setStatut(StatutTermint statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return nomMission; // Affiche uniquement le nom de la mission
    }
}
