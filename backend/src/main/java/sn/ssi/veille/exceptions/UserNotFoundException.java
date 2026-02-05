package sn.ssi.veille.exceptions;

/**
 * Exception levée quand un utilisateur n'est pas trouvé.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String id) {
        super("Utilisateur", "id", id);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("email: " + email);
    }

    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException("username: " + username);
    }
}
