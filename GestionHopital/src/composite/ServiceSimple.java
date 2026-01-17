package composite;

public class ServiceSimple implements ServiceMedical {

    private final String nom;

    public ServiceSimple(String nom) {
        this.nom = nom;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void afficher() {
        System.out.println("Service : " + nom);
    }
}
