package sn.ssi.veille.exceptions;

/**
 * Exception levée quand un username est déjà utilisé.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class UsernameAlreadyExistsException extends ResourceAlreadyExistsException {

    public UsernameAlreadyExistsException(String username) {
        super("Un compte existe déjà avec le nom d'utilisateur : " + username);
    }
}
