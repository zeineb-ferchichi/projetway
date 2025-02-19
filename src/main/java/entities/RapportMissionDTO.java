package entities;

import java.time.LocalDate;
import java.util.List;

public class RapportMissionDTO {
    private int idRapport;
    private String libelleR;
    private LocalDate dateExpo;
    private List<String> ressources; // Liste des fichiers associés
    private Mission mission;

    public RapportMissionDTO(int idRapport, String libelleR, LocalDate dateExpo, List<String> ressources, Mission mission) {
        this.idRapport = idRapport;
        this.libelleR = libelleR;
        this.dateExpo = dateExpo;
        this.ressources = ressources;
        this.mission = mission;
    }

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
        return "RapportMissionDTO{" +
                "idRapport=" + idRapport +
                ", libelleR='" + libelleR + '\'' +
                ", dateExpo=" + dateExpo +
                ", ressources=" + ressources +
                ", mission=" + (mission != null ? mission.getIdMission() : "null") +
                '}';
    }
}
