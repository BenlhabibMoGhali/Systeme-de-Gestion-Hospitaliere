package state;

import model.RendezVous;

public class ProgrammeState implements RendezVousState {

    @Override
    public void suivant(RendezVous rendezVous) {
        rendezVous.setState(new EnCoursState());
    }

    @Override
    public String getEtat() {
        return "Programmé";
    }
}
