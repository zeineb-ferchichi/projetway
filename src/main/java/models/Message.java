package models;

import java.sql.Date;

public class Message {
    private int idmessage;
    private String contenu;
    private Date dateEnvoi;
    private int idforum;  // Clé étrangère (référence à forum)

    // Constructeurs
    public Message() {}

    public Message(int idmessage, String contenu, Date dateEnvoi, int idforum) {
        this.idmessage = idmessage;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.idforum = idforum;  // Initialisation de la clé étrangère
    }

    public Message(int idmessage, String contenu, Date dateEnvoi) {
        this.idmessage = idmessage;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
    }

    public Message(String contenu, Date dateEnvoi, int idforum) {
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
        this.idforum = idforum;  // Initialisation de la clé étrangère
    }

    // Getters et Setters
    public int getIdmessage() {
        return idmessage;
    }

    public void setIdmessage(int idmessage) {
        this.idmessage = idmessage;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public int getIdforum() {
        return idforum;  // Retourne l'idforum (clé étrangère)
    }

    public void setIdforum(int idforum) {
        this.idforum = idforum;  // Modifie l'idforum (clé étrangère)
    }

    @Override
    public String toString() {
        return "Message{" +
                "idmessage=" + idmessage +
                ", contenu='" + contenu + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                ", idforum=" + idforum +  // Affiche l'idforum (clé étrangère)
                '}';
    }
}