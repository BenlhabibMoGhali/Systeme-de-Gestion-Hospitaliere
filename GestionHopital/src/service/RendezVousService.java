package service;

import model.RendezVous;
import model.Medecin;
import observer.Observer;
import observer.Subject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousService implements Subject {

    private final List<RendezVous> rendezVousList = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();

    /* =======================
       OBSERVER PATTERN
       ======================= */

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    /* =======================
       RENDEZ-VOUS
       ======================= */

    /**
     * Prendre un rendez-vous (réservation du créneau via agenda du médecin)
     */
    public synchronized RendezVous prendreRendezVous(RendezVous rendezVous) {

        LocalDateTime date = rendezVous.getDate();
        Medecin medecin = rendezVous.getMedecin();

        if (!medecin.estDisponible(date)) {
            throw new IllegalStateException("Créneau indisponible.");
        }

        medecin.reserverCreneau(date);
        rendezVousList.add(rendezVous);

        notifyObservers(
                "Rendez-vous programmé le " + date +
                        " pour le patient " + rendezVous.getPatient().getNom()
        );

        return rendezVous;
    }

    /**
     * Annuler un rendez-vous (interdit si déjà En cours ou Terminé)
     */
    public synchronized void annulerRendezVous(RendezVous rendezVous) {

        if (!rendezVousList.contains(rendezVous)) {
            throw new IllegalArgumentException("Rendez-vous introuvable.");
        }

        String etat = rendezVous.getEtat();
        if ("En cours".equals(etat) || "Terminé".equals(etat)) {
            throw new IllegalStateException("Impossible d'annuler un rendez-vous " + etat + ".");
        }

        Medecin medecin = rendezVous.getMedecin();
        medecin.libererCreneau(rendezVous.getDate());

        rendezVous.setState(new state.AnnuleState());
        rendezVousList.remove(rendezVous);

        notifyObservers(
                "Rendez-vous annulé pour le patient " +
                        rendezVous.getPatient().getNom()
        );
    }

    /**
     * Reporter un rendez-vous (interdit si Annulé ou Terminé)
     */
    public synchronized void reporterRendezVous(
            RendezVous rendezVous,
            LocalDateTime nouvelleDate
    ) {

        if (!rendezVousList.contains(rendezVous)) {
            throw new IllegalArgumentException("Rendez-vous introuvable.");
        }

        String etat = rendezVous.getEtat();
        if ("Annulé".equals(etat) || "Terminé".equals(etat)) {
            throw new IllegalStateException("Impossible de reporter un rendez-vous " + etat + ".");
        }

        Medecin medecin = rendezVous.getMedecin();

        // libérer ancien créneau
        medecin.libererCreneau(rendezVous.getDate());

        if (!medecin.estDisponible(nouvelleDate)) {
            // remettre l’ancien créneau occupé si report impossible
            medecin.reserverCreneau(rendezVous.getDate());
            throw new IllegalStateException("Nouveau créneau indisponible.");
        }

        rendezVous.changerDate(nouvelleDate);
        medecin.reserverCreneau(nouvelleDate);

        notifyObservers(
                "Rendez-vous reporté au " + nouvelleDate +
                        " pour le patient " +
                        rendezVous.getPatient().getNom()
        );
    }

    /**
     * Avancer l’état (State Pattern)
     */
    public void avancerEtatRendezVous(RendezVous rendezVous) {
        rendezVous.suivantEtat();
    }

    public List<RendezVous> getTousLesRendezVous() {
        return new ArrayList<>(rendezVousList);
    }
}
