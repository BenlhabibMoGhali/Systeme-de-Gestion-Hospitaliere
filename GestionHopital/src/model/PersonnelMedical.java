package model;

public abstract class PersonnelMedical {

    protected final String nom;
    protected final String prenom;

    public PersonnelMedical(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
