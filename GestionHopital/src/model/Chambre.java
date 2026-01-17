package model;

import java.util.ArrayList;
import java.util.List;

public class Chambre {

    private final int numero;
    private final ChambreType type;
    private final List<Lit> lits = new ArrayList<>();
    private boolean enNettoyage;

    public Chambre(int numero, ChambreType type, int nbLits) {
        this.numero = numero;
        this.type = type;
        for (int i = 1; i <= nbLits; i++) {
            lits.add(new Lit(i));
        }
        this.enNettoyage = false;
    }

    public int getNumero() {
        return numero;
    }

    public ChambreType getType() {
        return type;
    }

    public List<Lit> getLits() {
        return lits;
    }

    public boolean estEnNettoyage() {
        return enNettoyage;
    }

    public void setEnNettoyage(boolean enNettoyage) {
        this.enNettoyage = enNettoyage;
    }

    public Lit trouverLitLibre() {
        if (enNettoyage) return null;
        for (Lit lit : lits) {
            if (!lit.estOccupe()) return lit;
        }
        return null;
    }
}
