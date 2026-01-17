package state;

import model.RendezVous;

public interface RendezVousState {

    void suivant(RendezVous rendezVous);
    String getEtat();
}
