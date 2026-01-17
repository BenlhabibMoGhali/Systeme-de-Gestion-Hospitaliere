package service;

import model.Consultation;
import model.Medecin;
import model.Patient;
import model.Prescription;

import java.time.LocalDateTime;

public class ConsultationService {

    public Consultation creerConsultation(
            Patient patient,
            Medecin medecin,
            String symptomes,
            String diagnostic
    ) {
        Consultation consultation = new Consultation(
                LocalDateTime.now(),
                medecin,
                symptomes,
                diagnostic
        );

        patient.ajouterConsultation(consultation);
        return consultation;
    }

    /* =======================
       ÉNONCÉ : prescriptions / examens
       ======================= */

    public void ajouterPrescription(Consultation consultation, String medicament, String dosage) {
        consultation.ajouterPrescription(new Prescription(medicament, dosage));
    }

    public void ajouterResultatAnalyse(Consultation consultation, String nomAnalyse, String contenuOuChemin) {
        consultation.ajouterResultatAnalyse(nomAnalyse, contenuOuChemin);
    }

    public void definirNotesConfidentielles(Consultation consultation, String notes) {
        consultation.definirNotesConfidentielles(notes);
    }
}
