package models;

public enum TypeAbonnement {
    MENSUEL,
    ANNUEL,
    HEBDOMADAIRE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
