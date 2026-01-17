package model;

import java.time.LocalDateTime;

public class UrgenceRendezVous extends RendezVous {

    private final int priorite; // 1 = critique (selon ton NiveauUrgence)

    public UrgenceRendezVous(LocalDateTime date, Patient patient, Medecin medecin, int priorite) {
        super(date, patient, medecin);
        this.priorite = priorite;
    }

    public int getPriorite() {
        return priorite;
    }
}
