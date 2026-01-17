package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Medecin extends PersonnelMedical {

    private String specialite;
    private String numeroINPE;

    // Médecin de garde
    private boolean medecinDeGarde;

    // Agenda du médecin : créneau -> disponible ou non
    private final Map<LocalDateTime, Boolean> agenda;

    public Medecin(String nom, String prenom, String specialite, String numeroINPE) {
        super(nom, prenom);
        this.specialite = specialite;
        this.numeroINPE = numeroINPE;
        this.agenda = new TreeMap<>();
    }

    /* =======================
       GESTION DE L’AGENDA
       ======================= */

    // Initialisation des créneaux de 30 minutes
    public synchronized void initialiserAgenda(LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime current = debut;
        while (current.isBefore(fin)) {
            agenda.put(current, true);
            current = current.plus(Duration.ofMinutes(30));
        }
    }

    public synchronized boolean estDisponible(LocalDateTime date) {
        return agenda.getOrDefault(date, false);
    }

    public synchronized void reserverCreneau(LocalDateTime date) {
        agenda.put(date, false);
    }

    public synchronized void libererCreneau(LocalDateTime date) {
        agenda.put(date, true);
    }

    // Retourne une vue non modifiable (évite modification externe non synchronisée)
    public synchronized Map<LocalDateTime, Boolean> getAgenda() {
        return Collections.unmodifiableMap(new TreeMap<>(agenda));
    }

    /* =======================
       GETTERS / SETTERS
       ======================= */

    public String getSpecialite() {
        return specialite;
    }

    public String getNumeroINPE() {
        return numeroINPE;
    }

    public boolean estMedecinDeGarde() {
        return medecinDeGarde;
    }

    public void setMedecinDeGarde(boolean medecinDeGarde) {
        this.medecinDeGarde = medecinDeGarde;
    }
}
