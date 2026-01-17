package service;

import model.Consultation;
import model.Facture;
import model.Medecin;
import model.Prescription;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatistiquesService {

    /* =======================
       CONSULTATIONS
       ======================= */

    public long nombreConsultations(List<Consultation> consultations) {
        return consultations == null ? 0 : consultations.size();
    }

    public Map<Medecin, Long> consultationsParMedecin(List<Consultation> consultations) {
        return consultations.stream()
                .collect(Collectors.groupingBy(
                        Consultation::getMedecin,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> consultationsParSpecialite(List<Consultation> consultations) {
        return consultations.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getMedecin().getSpecialite(),
                        Collectors.counting()
                ));
    }

    /* =======================
       MÉDICAMENTS
       ======================= */

    public Map<String, Long> medicamentsLesPlusPrescrits(List<Consultation> consultations) {
        return consultations.stream()
                .flatMap(c -> c.getPrescriptions().stream())
                .collect(Collectors.groupingBy(
                        Prescription::getMedicament,
                        Collectors.counting()
                ));
    }

    /* =======================
       FINANCIER
       ======================= */

    public double revenusTotaux(List<Facture> factures) {
        return factures.stream()
                .filter(Facture::estSoldee)
                .mapToDouble(Facture::getTotal) // ✅ évite recalcul side-effect
                .sum();
    }

    /* =======================
       URGENCES
       ======================= */

    public Duration tempsAttenteMoyenUrgences(
            List<LocalDateTime> arrivees,
            List<LocalDateTime> prisesEnCharge
    ) {
        if (arrivees == null || prisesEnCharge == null) return Duration.ZERO;
        if (arrivees.isEmpty()) return Duration.ZERO;

        int n = Math.min(arrivees.size(), prisesEnCharge.size());
        if (n == 0) return Duration.ZERO;

        long totalMinutes = 0;

        for (int i = 0; i < n; i++) {
            totalMinutes += Duration
                    .between(arrivees.get(i), prisesEnCharge.get(i))
                    .toMinutes();
        }

        return Duration.ofMinutes(totalMinutes / n);
    }
}
