package model;

public class Lit {

    private final int numero;
    private boolean occupe;

    public Lit(int numero) {
        this.numero = numero;
        this.occupe = false;
    }

    public int getNumero() {
        return numero;
    }

    public boolean estOccupe() {
        return occupe;
    }

    public void setOccupe(boolean occupe) {
        this.occupe = occupe;
    }
}
