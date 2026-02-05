package sn.ssi.veille.exceptions;

/**
 * Exception levée quand une notification n'est pas trouvée.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class NotificationNotFoundException extends ResourceNotFoundException {

    public NotificationNotFoundException(String id) {
        super("Notification", "id", id);
    }
}
