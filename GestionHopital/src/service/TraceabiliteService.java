package service;

import model.Medicament;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TraceabiliteService {

    private final List<String> journal = new LinkedList<>();

    public synchronized void enregistrer(Medicament m, int quantite, String action) {
        if (!m.estControle()) return; // Traçabilité uniquement pour médicaments contrôlés

        journal.add(
                LocalDateTime.now() + " | " + action +
                        " | " + m.getNom() + " (" + m.getCode() + ")" +
                        " | qte=" + quantite
        );
    }

    public synchronized List<String> getJournal() {
        return List.copyOf(journal);
    }
}
