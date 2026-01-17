package model;

import strategy.FacturationStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Facture {

    private final Patient patient;
    private final LocalDateTime date;
    private final List<Facturable> prestations;
    private final FacturationStrategy strategy;

    private double total;
    private double paye;

    public Facture(Patient patient, FacturationStrategy strategy) {
        if (patient == null || strategy == null) {
            throw new IllegalArgumentException("Patient et stratégie obligatoires.");
        }
        this.patient = patient;
        this.strategy = strategy;
        this.date = LocalDateTime.now();
        this.prestations = new ArrayList<>();
        this.total = 0;
        this.paye = 0;
    }

    /* =======================
       MÉTIER
       ======================= */

    public void ajouterPrestation(Facturable f) {
        if (f == null) throw new IllegalArgumentException("Prestation obligatoire.");
        prestations.add(f);
        recalculerTotal();
    }

    /**
     * ✅ Pour compatibilité avec ton Main
     * Recalcule et retourne le total actuel.
     */
    public double calculerTotal() {
        recalculerTotal();
        return total;
    }

    private void recalculerTotal() {
        double brut = prestations.stream()
                .mapToDouble(Facturable::calculerCout)
                .sum();
        total = strategy.appliquerCouverture(brut);
    }

    public void payer(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du paiement doit être positif.");
        }
        this.paye += montant;
    }

    public boolean estSoldee() {
        return paye >= total;
    }

    /* =======================
       GETTERS
       ======================= */

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public double getPaye() {
        return paye;
    }

    public List<Facturable> getPrestations() {
        return Collections.unmodifiableList(prestations);
    }

    /* =======================
       REÇU
       ======================= */

    public String genererRecu() {
        return """
                FACTURE
                Patient : %s
                Date : %s
                Total : %.2f
                Payé : %.2f
                Statut : %s
                """.formatted(
                patient.getNom(),
                date,
                total,
                paye,
                estSoldee() ? "SOLDÉE" : "EN ATTENTE"
        );
    }
}
