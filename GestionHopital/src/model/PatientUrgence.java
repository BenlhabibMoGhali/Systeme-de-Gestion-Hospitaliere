package model;

import java.time.LocalDateTime;

public class PatientUrgence implements Comparable<PatientUrgence> {

    private final Patient patient;
    private final NiveauUrgence niveauUrgence;
    private final LocalDateTime heureArrivee;

    public PatientUrgence(Patient patient, NiveauUrgence niveauUrgence) {
        this.patient = patient;
        this.niveauUrgence = niveauUrgence;
        this.heureArrivee = LocalDateTime.now();
    }

    public Patient getPatient() {
        return patient;
    }

    public NiveauUrgence getNiveauUrgence() {
        return niveauUrgence;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    @Override
    public int compareTo(PatientUrgence autre) {
        int cmp = Integer.compare(
                this.niveauUrgence.getPriorite(),
                autre.niveauUrgence.getPriorite()
        );
        return (cmp != 0) ? cmp : this.heureArrivee.compareTo(autre.heureArrivee);
    }
}
