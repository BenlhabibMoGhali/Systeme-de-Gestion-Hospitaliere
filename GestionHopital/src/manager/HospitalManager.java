package manager;

import model.Medecin;
import model.Patient;
import observer.NotificationService;
import service.*;

import java.util.ArrayList;
import java.util.List;

public class HospitalManager {

    private static HospitalManager instance;

    /* =======================
       DONNÉES PRINCIPALES
       ======================= */

    private final List<Patient> patients = new ArrayList<>();
    private final List<Medecin> medecins = new ArrayList<>();

    /* =======================
       SERVICES MÉTIER
       ======================= */

    private final RendezVousService rendezVousService = new RendezVousService();
    private final ConsultationService consultationService = new ConsultationService();
    private final StatistiquesService statistiquesService = new StatistiquesService();
    private final FacturationService facturationService = new FacturationService();

    private final TraceabiliteService traceabiliteService = new TraceabiliteService();
    private final PharmacieService pharmacieService = new PharmacieService(traceabiliteService);

    private final UrgenceService urgenceService;

    // 🔥 NOUVEAUX SERVICES (A & B)
    private final ChambreService chambreService = new ChambreService();
    private final PersistenceService persistenceService =
            new PersistenceService("data");

    /* =======================
       NOTIFICATIONS
       ======================= */

    private final NotificationService notificationService =
            new NotificationService("Système Hôpital");

    private HospitalManager() {

        // Urgences dépendent des médecins
        this.urgenceService = new UrgenceService(medecins);

        /* =======================
           OBSERVERS
           ======================= */

        rendezVousService.addObserver(notificationService);
        urgenceService.addObserver(notificationService);
        pharmacieService.addObserver(notificationService);
        chambreService.addObserver(notificationService);

        /* =======================
           THREADS
           ======================= */

        Thread stockThread = new Thread(pharmacieService);
        stockThread.setDaemon(true);
        stockThread.start();
    }

    public static synchronized HospitalManager getInstance() {
        if (instance == null) {
            instance = new HospitalManager();
        }
        return instance;
    }

    /* =======================
       DONNÉES
       ======================= */

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Medecin> getMedecins() {
        return medecins;
    }

    public void ajouterPatient(Patient patient) {
        patients.add(patient);
    }

    public void ajouterMedecin(Medecin medecin) {
        medecins.add(medecin);
    }

    /* =======================
       SERVICES
       ======================= */

    public RendezVousService getRendezVousService() {
        return rendezVousService;
    }

    public ConsultationService getConsultationService() {
        return consultationService;
    }

    public StatistiquesService getStatistiquesService() {
        return statistiquesService;
    }

    public FacturationService getFacturationService() {
        return facturationService;
    }

    public PharmacieService getPharmacieService() {
        return pharmacieService;
    }

    public TraceabiliteService getTraceabiliteService() {
        return traceabiliteService;
    }

    public UrgenceService getUrgenceService() {
        return urgenceService;
    }

    // 🔥 NOUVEAUX GETTERS (A & B)
    public ChambreService getChambreService() {
        return chambreService;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }
}
