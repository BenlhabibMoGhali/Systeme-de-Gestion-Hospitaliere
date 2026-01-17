package model;

import java.time.LocalDateTime;

public class ChirurgieRendezVous extends RendezVous {

    public ChirurgieRendezVous(
            LocalDateTime date,
            Patient patient,
            Medecin medecin
    ) {
        super(date, patient, medecin);
    }
}
