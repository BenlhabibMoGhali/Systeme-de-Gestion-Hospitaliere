package service;

import model.Facture;
import model.Facturable;
import model.Patient;
import strategy.FacturationStrategy;

import java.util.*;

public class FacturationService {

    private final Map<Patient, List<Facture>> historique = new HashMap<>();

    public Facture creerFacture(Patient patient, FacturationStrategy strategy) {
        if (patient == null || strategy == null) {
            throw new IllegalArgumentException("Patient et stratégie obligatoires.");
        }

        Facture facture = new Facture(patient, strategy);
        historique.computeIfAbsent(patient, p -> new LinkedList<>()).add(facture);
        return facture;
    }

    public void ajouterPrestation(Facture facture, Facturable facturable) {
        if (facture == null || facturable == null) {
            throw new IllegalArgumentException("Facture et prestation obligatoires.");
        }
        facture.ajouterPrestation(facturable);
    }

    public void payer(Facture facture, double montant) {
        if (facture == null) {
            throw new IllegalArgumentException("Facture obligatoire.");
        }
        facture.payer(montant);
    }

    public List<Facture> getHistorique(Patient patient) {
        List<Facture> factures = historique.getOrDefault(patient, List.of());
        return Collections.unmodifiableList(factures);
    }

    // Utile pour rapports/statistiques
    public List<Facture> getToutesLesFactures() {
        return historique.values().stream()
                .flatMap(List::stream)
                .toList();
    }
}
