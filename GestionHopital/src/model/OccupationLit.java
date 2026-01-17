package model;

import java.time.LocalDateTime;

public class OccupationLit {

    private final Patient patient;
    private final Chambre chambre;
    private final Lit lit;
    private final LocalDateTime dateEntree;

    public OccupationLit(Patient patient, Chambre chambre, Lit lit) {
        this.patient = patient;
        this.chambre = chambre;
        this.lit = lit;
        this.dateEntree = LocalDateTime.now();
    }

    public Patient getPatient() {
        return patient;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public Lit getLit() {
        return lit;
    }

    public LocalDateTime getDateEntree() {
        return dateEntree;
    }
}
