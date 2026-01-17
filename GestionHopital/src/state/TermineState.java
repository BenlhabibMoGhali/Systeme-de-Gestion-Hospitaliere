package state;

import model.RendezVous;

public class TermineState implements RendezVousState {

    @Override
    public void suivant(RendezVous rendezVous) {
        // Aucun état suivant : rendez-vous terminé
        // On ne fait rien
    }

    @Override
    public String getEtat() {
        return "Terminé";
    }
}
