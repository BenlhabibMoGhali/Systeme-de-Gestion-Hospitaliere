package service;

import model.*;
import observer.Observer;
import observer.Subject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class UrgenceService implements Subject {

    private final Queue<PatientUrgence> fileUrgence = new PriorityQueue<>();
    private final List<Observer> observers = new ArrayList<>();
    private final List<Medecin> medecins;

    public UrgenceService(List<Medecin> medecins) {
        this.medecins = medecins;
    }

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
        for (Observer o : observers) o.update(message);
    }

    public synchronized void enregistrerUrgence(Patient patient, NiveauUrgence niveauUrgence) {
        PatientUrgence pu = new PatientUrgence(patient, niveauUrgence);
        fileUrgence.add(pu);

        notifyObservers("🚨 Urgence enregistrée : " + patient.getNom() + " (" + niveauUrgence + ")");
    }

    public synchronized RendezVous traiterUrgence() {

        if (fileUrgence.isEmpty()) {
            throw new IllegalStateException("Aucune urgence en attente.");
        }

        PatientUrgence pu = fileUrgence.poll();
        Medecin medecin = trouverMedecinDeGarde();

        RendezVous urgenceRv = RendezVousFactory.creerRendezVous(
                RendezVousType.URGENCE,
                LocalDateTime.now(),
                pu.getPatient(),
                medecin,
                pu.getNiveauUrgence().getPriorite()
        );

        notifyObservers("✅ Urgence traitée : " + pu.getPatient().getNom() + " par Dr " + medecin.getNom());

        return urgenceRv;
    }

    private Medecin trouverMedecinDeGarde() {
        return medecins.stream()
                .filter(Medecin::estMedecinDeGarde)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Aucun médecin de garde disponible."));
    }
}
