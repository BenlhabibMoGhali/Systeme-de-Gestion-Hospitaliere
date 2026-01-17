package composite;

import java.util.ArrayList;
import java.util.List;

public class ServiceComposite implements ServiceMedical {

    private final String nom;
    private final List<ServiceMedical> services = new ArrayList<>();

    public ServiceComposite(String nom) {
        this.nom = nom;
    }

    @Override
    public String getNom() {
        return nom;
    }

    public void ajouter(ServiceMedical service) {
        services.add(service);
    }

    public void supprimer(ServiceMedical service) {
        services.remove(service);
    }

    @Override
    public void afficher() {
        afficherAvecIndentation(0);
    }

    // Affichage hiérarchique (plus lisible)
    private void afficherAvecIndentation(int niveau) {
        String indent = "  ".repeat(niveau);
        System.out.println(indent + "Groupe : " + nom);

        for (ServiceMedical service : services) {
            if (service instanceof ServiceComposite sc) {
                sc.afficherAvecIndentation(niveau + 1);
            } else {
                System.out.println("  ".repeat(niveau + 1) + "Service : " + service.getNom());
            }
        }
    }
}
