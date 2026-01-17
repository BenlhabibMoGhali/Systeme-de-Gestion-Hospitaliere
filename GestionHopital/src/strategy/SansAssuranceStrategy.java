package strategy;

public class SansAssuranceStrategy implements FacturationStrategy {

    @Override
    public double appliquerCouverture(double montant) {
        return montant;
    }
}
