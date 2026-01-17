package model;

import java.time.LocalDateTime;
import java.util.*;

public class Consultation implements Facturable {

    private final LocalDateTime date;
    private final Medecin medecin;

    private final String symptomes;
    private final String diagnostic;

    // Notes médicales confidentielles (énoncé)
    private String notesConfidentielles;

    // Résultats d'analyses attachés (énoncé) : nomAnalyse -> contenu/chemin
    private final Map<String, String> resultatsAnalyses = new HashMap<>();

    // Prescriptions sans doublons (énoncé)
    private final Set<Prescription> prescriptions = new HashSet<>();

    // Tarification
    private static final double TARIF_CONSULTATION = 150;

    public Consultation(LocalDateTime date, Medecin medecin,
                        String symptomes, String diagnostic) {
        this.date = date;
        this.medecin = medecin;
        this.symptomes = symptomes;
        this.diagnostic = diagnostic;
    }

    /* =======================
       GETTERS
       ======================= */

    public LocalDateTime getDate() {
        return date;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public String getSymptomes() {
        return symptomes;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    // Vue non modifiable : évite modification externe
    public Set<Prescription> getPrescriptions() {
        return Collections.unmodifiableSet(prescriptions);
    }

    public String getNotesConfidentielles() {
        return notesConfidentielles;
    }

    public Map<String, String> getResultatsAnalyses() {
        return Collections.unmodifiableMap(resultatsAnalyses);
    }

    /* =======================
       MÉTIER
       ======================= */

    public void ajouterPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public void ajouterResultatAnalyse(String nomAnalyse, String contenuOuChemin) {
        resultatsAnalyses.put(nomAnalyse, contenuOuChemin);
    }

    public void definirNotesConfidentielles(String notes) {
        this.notesConfidentielles = notes;
    }

    /* =======================
       FACTURATION
       ======================= */

    @Override
    public double calculerCout() {
        return TARIF_CONSULTATION;
    }
}
