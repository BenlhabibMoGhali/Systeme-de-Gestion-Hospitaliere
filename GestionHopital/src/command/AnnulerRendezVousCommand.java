package command;

import model.RendezVous;
import service.RendezVousService;

public class AnnulerRendezVousCommand implements Command {

    private final RendezVousService rendezVousService;
    private final RendezVous rendezVous;

    public AnnulerRendezVousCommand(RendezVousService rendezVousService, RendezVous rendezVous) {
        if (rendezVousService == null || rendezVous == null) {
            throw new IllegalArgumentException("Service et rendez-vous obligatoires.");
        }
        this.rendezVousService = rendezVousService;
        this.rendezVous = rendezVous;
    }

    @Override
    public void execute() {
        rendezVousService.annulerRendezVous(rendezVous);
    }
}
