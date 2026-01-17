package service;

import model.*;
import observer.Observer;
import observer.Subject;

import java.util.*;

public class ChambreService implements Subject {

    private final List<Chambre> chambres = new ArrayList<>();
    private final Map<Integer, OccupationLit> occupationParPatientId = new HashMap<>();
    private final List<Observer> observers = new ArrayList<>();

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
       GESTION DES CHAMBRES
       ======================= */

    public synchronized void ajouterChambre(Chambre chambre) {
        if (chambre == null) {
            throw new IllegalArgumentException("Chambre obligatoire.");
        }
        chambres.add(chambre);
    }

    public synchronized List<Chambre> getChambres() {
        return List.copyOf(chambres);
    }

    /* =======================
       ATTRIBUTION DE LIT
       ======================= */

    public synchronized OccupationLit attribuerLit(Patient patient, NiveauUrgence niveauUrgence) {

        if (occupationParPatientId.containsKey(patient.getId())) {
            throw new IllegalStateException(
                    "Patient déjà hospitalisé : " + patient.getNom()
            );
        }

        List<ChambreType> ordre = ordreTypesSelonUrgence(niveauUrgence);

        for (ChambreType type : ordre) {
            for (Chambre chambre : chambres) {

                if (chambre.getType() != type) continue;

                Lit litLibre = chambre.trouverLitLibre();
                if (litLibre != null) {

                    litLibre.setOccupe(true);

                    OccupationLit occupation = new OccupationLit(
                            patient, chambre, litLibre
                    );
                    occupationParPatientId.put(patient.getId(), occupation);

                    notifyObservers(
                            "🏥 Lit attribué : patient " + patient.getNom() +
                                    " → chambre " + chambre.getNumero() +
                                    " (" + chambre.getType() + "), lit " +
                                    litLibre.getNumero()
                    );

                    return occupation;
                }
            }
        }

        throw new IllegalStateException(
                "Aucun lit disponible pour niveau d'urgence : " + niveauUrgence
        );
    }

    /* =======================
       TRANSFERT DE PATIENT
       ======================= */

    public synchronized OccupationLit transfererPatient(
            Patient patient,
            ChambreType nouveauType
    ) {

        OccupationLit actuelle = occupationParPatientId.get(patient.getId());
        if (actuelle == null) {
            throw new IllegalStateException(
                    "Patient non hospitalisé : " + patient.getNom()
            );
        }

        Chambre chambreCible = null;
        Lit litLibre = null;

        for (Chambre chambre : chambres) {
            if (chambre.getType() == nouveauType) {
                Lit candidat = chambre.trouverLitLibre();
                if (candidat != null) {
                    chambreCible = chambre;
                    litLibre = candidat;
                    break;
                }
            }
        }

        if (chambreCible == null || litLibre == null) {
            throw new IllegalStateException(
                    "Aucun lit disponible pour transfert vers : " + nouveauType
            );
        }

        // Libérer ancien lit
        actuelle.getLit().setOccupe(false);

        // Occuper nouveau lit
        litLibre.setOccupe(true);

        OccupationLit nouvelleOccupation = new OccupationLit(
                patient, chambreCible, litLibre
        );
        occupationParPatientId.put(patient.getId(), nouvelleOccupation);

        notifyObservers(
                "🔁 Transfert patient " + patient.getNom() +
                        " → chambre " + chambreCible.getNumero() +
                        " (" + chambreCible.getType() + "), lit " +
                        litLibre.getNumero()
        );

        return nouvelleOccupation;
    }

    /* =======================
       SORTIE & NETTOYAGE
       ======================= */

    public synchronized void libererLit(Patient patient) {

        OccupationLit occupation = occupationParPatientId.remove(patient.getId());
        if (occupation == null) {
            throw new IllegalStateException(
                    "Patient non hospitalisé : " + patient.getNom()
            );
        }

        occupation.getLit().setOccupe(false);
        occupation.getChambre().setEnNettoyage(true);

        notifyObservers(
                "🧼 Sortie patient " + patient.getNom() +
                        " → chambre " + occupation.getChambre().getNumero() +
                        " en nettoyage"
        );
    }

    public synchronized void terminerNettoyage(int numeroChambre) {

        Chambre chambre = chambres.stream()
                .filter(c -> c.getNumero() == numeroChambre)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Chambre introuvable : " + numeroChambre
                        )
                );

        chambre.setEnNettoyage(false);
        notifyObservers(
                "✅ Nettoyage terminé : chambre " + numeroChambre
        );
    }

    /* =======================
       LOGIQUE D'URGENCE
       ======================= */

    private List<ChambreType> ordreTypesSelonUrgence(NiveauUrgence niveau) {
        return switch (niveau) {
            case CRITIQUE, ELEVE ->
                    List.of(
                            ChambreType.SOINS_INTENSIFS,
                            ChambreType.VIP,
                            ChambreType.STANDARD
                    );
            case MOYEN ->
                    List.of(
                            ChambreType.VIP,
                            ChambreType.STANDARD
                    );
            case FAIBLE ->
                    List.of(
                            ChambreType.STANDARD,
                            ChambreType.VIP
                    );
        };
    }
}
