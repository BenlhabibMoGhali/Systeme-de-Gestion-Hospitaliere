package main;

import command.AnnulerRendezVousCommand;
import command.CommandInvoker;
import composite.ServiceComposite;
import composite.ServiceSimple;
import manager.HospitalManager;
import model.*;
import observer.NotificationService;
import service.*;
import strategy.AssurancePartielleStrategy;
import strategy.SansAssuranceStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {


        // 1) Singleton Manager

        HospitalManager hm = HospitalManager.getInstance();
        HospitalManager pt =HospitalManager.getInstance();

        // 2) Notifications (Observer)

        NotificationService notifAdmin = new NotificationService("Admin");
        hm.getRendezVousService().addObserver(notifAdmin);
        hm.getUrgenceService().addObserver(notifAdmin);
        hm.getPharmacieService().addObserver(notifAdmin);
        hm.getChambreService().addObserver(notifAdmin); // ✅ A

        // 2-bis) Chargement (Persistance B)

        System.out.println("\n=== Persistance : chargement des données ===");
        List<Patient> patientsCharges = hm.getPersistenceService().loadPatients();
        patientsCharges.forEach(hm::ajouterPatient);

        List<Chambre> chambresChargees = hm.getPersistenceService().loadChambres();
        for (Chambre c : chambresChargees) {
            hm.getChambreService().ajouterChambre(c);
        }

        List<Medicament> medsCharges = hm.getPersistenceService().loadMedicaments();
        for (Medicament m : medsCharges) {
            hm.getPharmacieService().ajouterMedicament(m);
        }

        System.out.println("Patients chargés : " + patientsCharges.size());
        System.out.println("Chambres chargées : " + chambresChargees.size());
        System.out.println("Médicaments chargés : " + medsCharges.size());


        // 3) Données de base (si CSV vide)

        Patient p1;
        if (hm.getPatients().isEmpty()) {
            p1 = new Patient(1, "Fassi Fihri", "Ghali", "CNSS");
            p1.setMutuelle("AXA");
            p1.setGroupeSanguin("A+");
            p1.setAllergies("Pollen");
            p1.setAntecedents("Asthme léger");
            p1.setContactUrgenceNom("Si ammamou");
            p1.setContactUrgenceTel("0600000000");
            hm.ajouterPatient(p1);
        } else {
            p1 = hm.getPatients().get(0); // démo simple
        }

        Medecin m1 = new Medecin("Dr", "Amina", "Cardiologie", "INPE-123");
        m1.setMedecinDeGarde(true);

        // Agenda 9h -> 12h (créneaux 30 min)
        LocalDateTime debut = LocalDateTime.now()
                .withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fin = debut.withHour(12);
        m1.initialiserAgenda(debut, fin);
        hm.ajouterMedecin(m1);


        // 3-bis) Chambres/Lits (A) - si CSV vide

        if (hm.getChambreService().getChambres().isEmpty()) {
            hm.getChambreService().ajouterChambre(new Chambre(101, ChambreType.STANDARD, 2));
            hm.getChambreService().ajouterChambre(new Chambre(201, ChambreType.VIP, 1));
            hm.getChambreService().ajouterChambre(new Chambre(301, ChambreType.SOINS_INTENSIFS, 1));
        }

        // 4) Composite (services médicaux)

        ServiceComposite hopital = new ServiceComposite("Hôpital Central");
        ServiceComposite poleChirurgie = new ServiceComposite("Pôle Chirurgie");
        ServiceComposite urgences = new ServiceComposite("Service Urgences");

        poleChirurgie.ajouter(new ServiceSimple("Bloc opératoire"));
        poleChirurgie.ajouter(new ServiceSimple("Anesthésie"));

        urgences.ajouter(new ServiceSimple("Accueil"));
        urgences.ajouter(new ServiceSimple("Triage"));

        hopital.ajouter(poleChirurgie);
        hopital.ajouter(urgences);

        System.out.println("\n=== Composite : Hiérarchie des services ===");
        hopital.afficher();


        // 5) Rendez-vous (Factory + Service + State)

        LocalDateTime dateRv = debut.plusHours(1); // 10:00
        RendezVous rv1 = RendezVousFactory.creerRendezVous(
                RendezVousType.CONSULTATION,
                dateRv,
                p1,
                m1,
                0
        );

        System.out.println("\n=== Prise de rendez-vous ===");
        hm.getRendezVousService().prendreRendezVous(rv1);

        System.out.println("Etat RDV : " + rv1.getEtat());
        hm.getRendezVousService().avancerEtatRendezVous(rv1); // devient EN_COURS
        System.out.println("Etat RDV après suivantEtat : " + rv1.getEtat());

        // Reporter le RDV (test)
        System.out.println("\n=== Report rendez-vous ===");
        LocalDateTime nouvelleDate = debut.plusHours(2); // 11:00
        hm.getRendezVousService().reporterRendezVous(rv1, nouvelleDate);


        // 6) Command (annulation) - sur un autre RDV encore PROGRAMME

        System.out.println("\n=== Command : Annuler rendez-vous (PROGRAMME) ===");

        RendezVous rvAnnulable = RendezVousFactory.creerRendezVous(
                RendezVousType.CONSULTATION,
                debut.plusMinutes(30), // 09:30
                p1,
                m1,
                0
        );
        hm.getRendezVousService().prendreRendezVous(rvAnnulable);

        CommandInvoker invoker = new CommandInvoker();
        invoker.setCommand(new AnnulerRendezVousCommand(hm.getRendezVousService(), rvAnnulable));
        invoker.executeCommand();


        // 7) Urgences (PriorityQueue + médecin de garde)

        System.out.println("\n=== Urgences : Enregistrer et traiter ===");
        hm.getUrgenceService().enregistrerUrgence(p1, NiveauUrgence.CRITIQUE);
        RendezVous urgenceRv = hm.getUrgenceService().traiterUrgence();
        System.out.println("Urgence RDV créée, état = " + urgenceRv.getEtat());


        // 7-bis) Hospitalisation (A) : attribution auto + transfert + sortie + nettoyage
        hm.getChambreService().attribuerLit(p1, NiveauUrgence.CRITIQUE);

        try {
            hm.getChambreService().transfererPatient(p1, ChambreType.VIP);
        } catch (IllegalStateException e) {
            System.out.println("⚠ Transfert VIP impossible : " + e.getMessage());
            System.out.println("➡ Tentative transfert vers STANDARD...");
            try {
                hm.getChambreService().transfererPatient(p1, ChambreType.STANDARD);
            } catch (IllegalStateException ex) {
                System.out.println("⚠ Transfert STANDARD impossible : " + ex.getMessage());
            }
        }

        hm.getChambreService().libererLit(p1);
        hm.getChambreService().terminerNettoyage(301);



        // 8) Consultation + prescriptions + analyses + notes

        System.out.println("\n=== Consultation : diagnostic + prescription + analyses ===");
        ConsultationService cs = hm.getConsultationService();
        Consultation c1 = cs.creerConsultation(p1, m1, "Douleur thoracique", "Suspicion angine");
        cs.ajouterPrescription(c1, "Aspirine", "100mg/jour");
        cs.ajouterPrescription(c1, "Paracétamol", "500mg si douleur");
        cs.ajouterResultatAnalyse(c1, "ECG", "Normal");
        cs.definirNotesConfidentielles(c1, "Surveillance recommandée.");


        // 9) Facturation (Strategy + paiements partiels + reçu)

        System.out.println("\n=== Facturation : strategies + paiement partiel ===");
        FacturationService fs = hm.getFacturationService();

        Facture f1 = fs.creerFacture(p1, new SansAssuranceStrategy());
        fs.ajouterPrestation(f1, c1);
        f1.calculerTotal();
        fs.payer(f1, 50);
        fs.payer(f1, 200);
        System.out.println(f1.genererRecu());

        Facture f2 = fs.creerFacture(p1, new AssurancePartielleStrategy(0.7));
        fs.ajouterPrestation(f2, c1);
        f2.calculerTotal();
        fs.payer(f2, 30);
        System.out.println(f2.genererRecu());


        // 10) Pharmacie (stock + alertes + traçabilité + thread)

        System.out.println("\n=== Pharmacie : stock + délivrance + traçabilité ===");
        PharmacieService ps = hm.getPharmacieService();

        // si stock vide (après load) on ajoute un médoc demo
        if (ps.getTousLesMedicaments().isEmpty()) {
            Medicament med1 = new Medicament(
                    "Morphine",
                    "MED-001",
                    6,
                    LocalDate.now().plusDays(10),
                    true
            );
            ps.ajouterMedicament(med1);
        }

        ps.delivrer("MED-001", 2);
        ps.delivrer("MED-001", 2);

        System.out.println("\nJournal traçabilité (médicaments contrôlés) :");
        hm.getTraceabiliteService().getJournal().forEach(System.out::println);


        // 11) Statistiques

        System.out.println("\n=== Statistiques ===");
        StatistiquesService stats = hm.getStatistiquesService();

        System.out.println("Nombre consultations patient : " + stats.nombreConsultations(p1.getConsultations()));
        System.out.println("Consultations par médecin : " + stats.consultationsParMedecin(p1.getConsultations()));
        System.out.println("Consultations par spécialité : " + stats.consultationsParSpecialite(p1.getConsultations()));
        System.out.println("Médicaments les plus prescrits : " + stats.medicamentsLesPlusPrescrits(p1.getConsultations()));
        System.out.println("Revenus (factures soldées) : " + stats.revenusTotaux(fs.getHistorique(p1)));


        // 12) Thread rappels RDV (24h) - démonstration

        System.out.println("\n=== Thread rappel RDV (démonstration) ===");
        RappelRendezVousThread rappelThread = new RappelRendezVousThread(
                hm.getRendezVousService().getTousLesRendezVous(),
                hm.getRendezVousService()
        );
        rappelThread.start();


        // 13) Persistance : sauvegarde (B)

        System.out.println("\n=== Persistance : sauvegarde des données ===");
        hm.getPersistenceService().savePatients(hm.getPatients());
        hm.getPersistenceService().saveChambres(hm.getChambreService().getChambres());
        hm.getPersistenceService().saveMedicaments(hm.getPharmacieService().getTousLesMedicaments());
        System.out.println("✅ Données sauvegardées dans le dossier: data/");



        System.out.println("\n✅ Démo terminée.");
    }
}
