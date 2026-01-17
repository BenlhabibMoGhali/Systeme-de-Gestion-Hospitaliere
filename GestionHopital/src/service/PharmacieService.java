package service;

import model.Medicament;
import observer.Observer;
import observer.Subject;

import java.time.LocalDateTime;
import java.util.*;

public class PharmacieService implements Subject, Runnable {

    private final Map<String, Medicament> stock = new HashMap<>();
    private final List<Observer> observers = new ArrayList<>();

    private final TraceabiliteService traceabiliteService;

    private static final int SEUIL_FAIBLE = 10;
    private static final int SEUIL_CRITIQUE = 5;
    private static final int QTE_COMMANDE_AUTO = 50;

    private volatile boolean running = true;

    public PharmacieService(TraceabiliteService traceabiliteService) {
        if (traceabiliteService == null) {
            throw new IllegalArgumentException("TraceabiliteService obligatoire.");
        }
        this.traceabiliteService = traceabiliteService;
    }

    /* =======================
       OBSERVER
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
        for (Observer o : observers) {
            o.update(message);
        }
    }

    /* =======================
       STOCK
       ======================= */

    /**
     * Ajoute un médicament au stock.
     * Si le code existe déjà, on cumule la quantité (plus réaliste).
     */
    public synchronized void ajouterMedicament(Medicament m) {
        if (m == null) throw new IllegalArgumentException("Médicament obligatoire.");

        Medicament existant = stock.get(m.getCode());
        if (existant == null) {
            stock.put(m.getCode(), m);
        } else {
            // même code => on additionne
            existant.incrementer(m.getQuantite());
        }
    }

    public synchronized Medicament getMedicament(String code) {
        return stock.get(code);
    }

    /**
     * ✅ Compatible PersistanceService + Main : renvoie une LISTE
     */
    public synchronized List<Medicament> getTousLesMedicaments() {
        return new ArrayList<>(stock.values());
    }

    /**
     * Délivrance : décrémente le stock, vérifie expiration, trace si contrôlé
     */
    public synchronized void delivrer(String code, int quantite) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Code invalide.");
        if (quantite <= 0) throw new IllegalArgumentException("Quantité invalide.");

        Medicament m = stock.get(code);
        if (m == null) throw new IllegalArgumentException("Médicament introuvable : " + code);

        if (m.estExpire()) {
            notifyObservers("⚠ Médicament expiré détecté : " + m.getNom() + " (" + m.getCode() + ")");
            throw new IllegalStateException("Médicament expiré : " + m.getNom());
        }

        // decrementer() de Medicament gère déjà stock insuffisant, mais on garde un message clair
        if (m.getQuantite() < quantite) {
            notifyObservers("⚠ Stock insuffisant pour : " + m.getNom() + " (" + m.getCode() + ")");
            throw new IllegalStateException("Stock insuffisant.");
        }

        m.decrementer(quantite);

        // Traçabilité UNIQUEMENT si contrôlé (énoncé)
        if (m.estControle()) {
            traceabiliteService.enregistrer(m, quantite, "DELIVRANCE");
        }

        // Alertes
        if (m.getQuantite() <= SEUIL_FAIBLE) {
            notifyObservers("🔻 Stock faible : " + m.getNom() + " = " + m.getQuantite());
        }

        // Commande auto si critique
        if (m.getQuantite() <= SEUIL_CRITIQUE) {
            commanderAutomatiquement(m);
        }
    }

    private synchronized void commanderAutomatiquement(Medicament m) {
        m.incrementer(QTE_COMMANDE_AUTO);
        notifyObservers("🛒 Commande automatique : +" + QTE_COMMANDE_AUTO + " pour " + m.getNom());

        // Traçabilité si contrôlé
        if (m.estControle()) {
            traceabiliteService.enregistrer(m, QTE_COMMANDE_AUTO, "COMMANDE_AUTO");
        }
    }

    /* =======================
       THREAD PERIODIQUE
       Vérifie stock faible + périmés
       ======================= */

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10_000); // toutes les 10s (démo)
                verifierAlertesAutomatiques();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    public void stop() {
        running = false;
    }

    private synchronized void verifierAlertesAutomatiques() {
        for (Medicament m : stock.values()) {

            if (m.estExpire()) {
                notifyObservers("⛔ Médicament expiré : " + m.getNom() + " (" + m.getCode() + ")");
            }

            if (m.getQuantite() <= SEUIL_FAIBLE) {
                notifyObservers("🔻 Stock faible : " + m.getNom() + " = " + m.getQuantite());
            }
        }

        notifyObservers("✅ Scan pharmacie terminé (" + LocalDateTime.now() + ")");
    }
}
