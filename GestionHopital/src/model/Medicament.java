package model;

import java.time.LocalDate;

public class Medicament {

    private final String nom;
    private final String code;
    private int quantite;
    private final LocalDate dateExpiration;
    private final boolean controle;

    public Medicament(String nom, String code, int quantite,
                      LocalDate dateExpiration, boolean controle) {

        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Nom du médicament invalide.");

        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Code du médicament invalide.");

        if (quantite < 0)
            throw new IllegalArgumentException("Quantité négative interdite.");

        if (dateExpiration == null)
            throw new IllegalArgumentException("Date d'expiration obligatoire.");

        this.nom = nom;
        this.code = code;
        this.quantite = quantite;
        this.dateExpiration = dateExpiration;
        this.controle = controle;
    }

    public String getNom() {
        return nom;
    }

    public String getCode() {
        return code;
    }

    public int getQuantite() {
        return quantite;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public boolean estControle() {
        return controle;
    }

    public boolean estExpire() {
        return dateExpiration.isBefore(LocalDate.now());
    }

    public void decrementer(int qte) {
        if (qte <= 0)
            throw new IllegalArgumentException("Quantité à décrémenter invalide.");
        if (quantite < qte)
            throw new IllegalStateException("Stock insuffisant.");
        this.quantite -= qte;
    }

    public void incrementer(int qte) {
        if (qte <= 0)
            throw new IllegalArgumentException("Quantité à incrémenter invalide.");
        this.quantite += qte;
    }
}
