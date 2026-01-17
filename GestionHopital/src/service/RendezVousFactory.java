package service;

import model.*;

import java.time.LocalDateTime;

public class RendezVousFactory {

    public static RendezVous creerRendezVous(
            RendezVousType type,
            LocalDateTime date,
            Patient patient,
            Medecin medecin,
            int priorite
    ) {

        switch (type) {

            case CONSULTATION:
                return new ConsultationRendezVous(
                        date,
                        patient,
                        medecin
                );

            case URGENCE:
                return new UrgenceRendezVous(
                        date,
                        patient,
                        medecin,
                        priorite
                );

            case CHIRURGIE:
                return new ChirurgieRendezVous(
                        date,
                        patient,
                        medecin
                );

            default:
                throw new IllegalArgumentException("Type de rendez-vous inconnu");
        }
    }
}
