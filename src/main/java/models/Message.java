package models;

import java.sql.Date;

public class Message {
    private int id;
    private String contenu;
    private Date dateEnvoi;

    // Constructeurs
    public Message() {}

    public Message(int id, String contenu, Date dateEnvoi) {
        this.id = id;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
    }

    public Message(String contenu, Date dateEnvoi) {
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                '}';
    }
}
