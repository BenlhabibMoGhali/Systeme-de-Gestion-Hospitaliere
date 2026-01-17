package model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Patient {

    private final int id;
    private final String nom;
    private final String prenom;

    // Dossier médical
    private String groupeSanguin;
    private String allergies;
    private String antecedents;

    // Dossier d'urgence (énoncé)
    private String contactUrgenceNom;
    private String contactUrgenceTel;

    // Assurance / mutuelle (énoncé)
    private String assuranceMaladie;
    private String mutuelle;

    // Historique médical chronologique (EXIGÉ)
    private final List<Consultation> consultations;

    public Patient(int id, String nom, String prenom, String assuranceMaladie) {

        if (id <= 0) throw new IllegalArgumentException("ID patient invalide.");
        if (nom == null || nom.isBlank()) throw new IllegalArgumentException("Nom invalide.");
        if (prenom == null || prenom.isBlank()) throw new IllegalArgumentException("Prénom invalide.");
        if (assuranceMaladie == null) assuranceMaladie = "";

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.assuranceMaladie = assuranceMaladie;
        this.consultations = new LinkedList<>();
    }

    /* =======================
       CONSULTATIONS
       ======================= */

    public void ajouterConsultation(Consultation consultation) {
        if (consultation == null) {
            throw new IllegalArgumentException("Consultation obligatoire.");
        }
        consultations.add(consultation);
    }

    // Vue non modifiable (évite modification externe)
    public List<Consultation> getConsultations() {
        return Collections.unmodifiableList(consultations);
    }

    // Optionnel : utile si plus tard tu veux persister/charger consultations
    // Accès package uniquement (pas public)
    List<Consultation> getConsultationsMutable() {
        return consultations;
    }

    /* =======================
       GETTERS / SETTERS
       ======================= */

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public String getAssuranceMaladie() {
        return assuranceMaladie;
    }

    public void setAssuranceMaladie(String assuranceMaladie) {
        this.assuranceMaladie = (assuranceMaladie == null) ? "" : assuranceMaladie;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public String getContactUrgenceNom() {
        return contactUrgenceNom;
    }

    public void setContactUrgenceNom(String contactUrgenceNom) {
        this.contactUrgenceNom = contactUrgenceNom;
    }

    public String getContactUrgenceTel() {
        return contactUrgenceTel;
    }

    public void setContactUrgenceTel(String contactUrgenceTel) {
        this.contactUrgenceTel = contactUrgenceTel;
    }

    /* =======================
       equals / hashCode
       ======================= */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return id == patient.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* =======================
       Debug / logs
       ======================= */

    @Override
    public String toString() {
        return "Patient{id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", assurance='" + assuranceMaladie + '\'' +
                ", mutuelle='" + (mutuelle == null ? "" : mutuelle) + '\'' +
                '}';
    }
}
