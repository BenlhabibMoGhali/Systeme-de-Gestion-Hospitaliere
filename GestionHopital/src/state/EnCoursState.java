package state;

import model.RendezVous;

public class EnCoursState implements RendezVousState {

    @Override
    public void suivant(RendezVous rendezVous) {
        rendezVous.setState(new TermineState());
    }

    @Override
    public String getEtat() {
        return "En cours";
    }
}
