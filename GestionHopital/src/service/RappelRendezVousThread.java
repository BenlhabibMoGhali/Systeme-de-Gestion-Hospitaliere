package service;

import model.RendezVous;
import observer.Subject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RappelRendezVousThread extends Thread {

    private final List<RendezVous> rendezVousList;
    private final Subject subject;

    // Pour éviter les rappels en double
    private final Set<RendezVous> dejaNotifies = new HashSet<>();

    public RappelRendezVousThread(List<RendezVous> rendezVousList, Subject subject) {
        this.rendezVousList = rendezVousList;
        this.subject = subject;
        setDaemon(true); // optionnel : s'arrête quand l'app s'arrête
    }

    @Override
    public void run() {

        while (!isInterrupted()) {

            // Copie défensive (évite ConcurrentModificationException)
            List<RendezVous> snapshot = List.copyOf(rendezVousList);

            LocalDateTime now = LocalDateTime.now();

            for (RendezVous rv : snapshot) {

                // déjà notifié => skip
                if (dejaNotifies.contains(rv)) continue;

                Duration d = Duration.between(now, rv.getDate());

                // si rdv déjà passé, ignore
                if (d.isNegative()) continue;

                long heures = d.toHours();

                // Fenêtre : entre 24h et 23h avant
                if (heures <= 24 && heures >= 23) {

                    subject.notifyObservers(
                            "Rappel : rendez-vous dans ~24h pour " +
                                    rv.getPatient().getNom() +
                                    " (le " + rv.getDate() + ")"
                    );

                    dejaNotifies.add(rv);
                }
            }

            try {
                Thread.sleep(60 * 60 * 1000); // vérification chaque heure
            } catch (InterruptedException e) {
                interrupt(); // restaure le flag
            }
        }
    }
}
