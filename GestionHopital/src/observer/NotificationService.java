package observer;

import java.time.LocalDateTime;

public class NotificationService implements Observer {

    private final String destinataire;

    public NotificationService(String destinataire) {
        this.destinataire = destinataire;
    }

    @Override
    public void update(String message) {
        enregistrerNotification(message);
    }

    /* =======================
       MÉTIER
       ======================= */

    private void enregistrerNotification(String message) {
        // Ici on simule une notification (console)
        // Plus tard : email, SMS, base de données, etc.
        System.out.println(
                "[" + LocalDateTime.now() + "] 🔔 Notification pour "
                        + destinataire + " : " + message
        );
    }
}
