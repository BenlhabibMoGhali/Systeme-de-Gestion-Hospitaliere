package model;

public enum NiveauUrgence {
    CRITIQUE(1),
    ELEVE(2),
    MOYEN(3),
    FAIBLE(4);

    private final int priorite;

    NiveauUrgence(int priorite) {
        this.priorite = priorite;
    }

    public int getPriorite() {
        return priorite;
    }
}
