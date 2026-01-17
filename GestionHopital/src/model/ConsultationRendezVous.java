package model;

import java.time.LocalDateTime;

public class ConsultationRendezVous extends RendezVous {

    public ConsultationRendezVous(LocalDateTime date, Patient patient, Medecin medecin) {
        super(date, patient, medecin);
    }
}
