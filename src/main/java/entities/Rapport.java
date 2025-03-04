package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Rapport {
    private int idRapport;
    private String libelleR;
    private LocalDate dateExpo;
    private List<String> ressources; // Changement : liste de fichiers
    private Mission mission;

    // Constructeurs
    public Rapport() {}

    public Rapport(int idRapport, String libelleR, LocalDate dateExpo, List<String> ressources, Mission mission) {
        this.idRapport = idRapport;
        this.libelleR = libelleR;
        this.dateExpo = dateExpo;
        this.ressources = ressources;
        this.mission = mission;
    }
    public Rapport(String libelleR, String fichier, LocalDate dateExpo) {
        this.libelleR = libelleR;
        this.dateExpo = dateExpo;
        this.ressources = new ArrayList<>();
        this.ressources.add(fichier);
        this.mission = new Mission(1, "Mission par défaut"); // ⚠ Remplace par une mission valide
    }

    public Rapport(String libelleR, LocalDate dateExpo, List<String> ressources, Mission mission) {
        this.libelleR = libelleR;
        this.dateExpo = dateExpo;
        this.ressources = ressources;
        this.mission = mission;
    }

    // Getters et Setters
    public int getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(int idRapport) {
        this.idRapport = idRapport;
    }

    public String getLibelleR() {
        return libelleR;
    }

    public void setLibelleR(String libelleR) {
        this.libelleR = libelleR;
    }

    public LocalDate getDateExpo() {
        return dateExpo;
    }
    public Rapport(int idRapport) {
        this.idRapport = idRapport;
    }

    public void setDateExpo(LocalDate dateExpo) {
        this.dateExpo = dateExpo;
    }

    public List<String> getRessources() {
        return ressources;
    }

    public void setRessources(List<String> ressources) {
        this.ressources = ressources;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public String toString() {
        return "Rapport{" +
                "idRapport=" + idRapport +
                ", libelleR='" + libelleR + '\'' +
                ", dateExpo=" + dateExpo +
                ", ressources=" + ressources +
                ", mission=" + mission +
                '}';
    }
}
