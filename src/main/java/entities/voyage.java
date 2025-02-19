package entities;

public class voyage {




    int idvoyage;
    String destination;
    String date_depart,date_retour;

    public voyage(){}
    public voyage(int idvoyage,String destination,String date_depart,String date_retour) {
        this.idvoyage = idvoyage;
        this.destination = destination;
        this.date_depart = date_depart;
        this.date_retour = date_retour;
    }

    public voyage(String destination,String  date_depart,String date_retour) {

        this.destination = destination;
        this.date_depart = date_depart;
        this.date_retour =date_retour ;
    }


    @Override
    public String toString() {
        return "voyage{" +
                "id=" + idvoyage +
                ", destination=" + destination +
                ", date depart='" + date_depart + '\'' +
                ", date retour='" + date_retour + '\'' +
                '}';
    }

    public int getIdvoyage() {
        return idvoyage;
    }

    public void setIdvoyage(int idvoyage) {
        this.idvoyage = idvoyage;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate_depart() {
        return date_depart;
    }

    public void setDate_depart(String date_depart) {
        this.date_depart = date_depart;
    }

    public String getDate_retour() {
        return date_retour;
    }

    public void setDate_retour(String date_retour) {
        this.date_retour = date_retour;
    }
}