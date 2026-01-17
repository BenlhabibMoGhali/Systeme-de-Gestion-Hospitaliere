package model;

import state.*;

import java.time.LocalDateTime;

public abstract class RendezVous {

    protected LocalDateTime date;
    protected Patient patient;
    protected Medecin medecin;

    // ✅ State Pattern
    private RendezVousState state;

    public RendezVous(LocalDateTime date, Patient patient, Medecin medecin) {
        this.date = date;
        this.patient = patient;
        this.medecin = medecin;
        this.state = new ProgrammeState(); // état initial
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void changerDate(LocalDateTime nouvelleDate) {
        this.date = nouvelleDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    // ✅ requis par tes classes state.*
    public void setState(RendezVousState state) {
        this.state = state;
    }

    public RendezVousState getState() {
        return state;
    }

    // Pour affichage simple dans Main
    public String getEtat() {
        return state.getEtat();
    }

    public void suivantEtat() {
        state.suivant(this);
    }

    public void annuler() {
        setState(new AnnuleState());
    }
}
