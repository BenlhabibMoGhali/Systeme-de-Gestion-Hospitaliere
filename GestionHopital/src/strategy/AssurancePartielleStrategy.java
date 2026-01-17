package strategy;

public class AssurancePartielleStrategy implements FacturationStrategy {

    private final double taux; // ex : 0.7 = 70%

    public AssurancePartielleStrategy(double taux) {
        if (taux < 0 || taux > 1) {
            throw new IllegalArgumentException("Le taux doit être entre 0 et 1.");
        }
        this.taux = taux;
    }

    @Override
    public double appliquerCouverture(double montant) {
        return montant * (1 - taux);
    }
}
