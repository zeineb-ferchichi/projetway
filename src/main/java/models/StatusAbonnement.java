package models;

public enum StatusAbonnement {
    ACTIF,
    EXPIRE,
    SUSPENDU;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
