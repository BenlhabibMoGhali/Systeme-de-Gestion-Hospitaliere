package state;

import model.RendezVous;

public class AnnuleState implements RendezVousState {

    @Override
    public void suivant(RendezVous rendezVous) {
        // Aucun état suivant : rendez-vous annulé
        // On ne fait rien
    }

    @Override
    public String getEtat() {
        return "Annulé";
    }
}
