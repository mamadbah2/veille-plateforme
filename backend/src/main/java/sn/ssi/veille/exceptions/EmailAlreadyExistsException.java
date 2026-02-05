package sn.ssi.veille.exceptions;

/**
 * Exception levée quand un email est déjà utilisé.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class EmailAlreadyExistsException extends ResourceAlreadyExistsException {

    public EmailAlreadyExistsException(String email) {
        super("Un compte existe déjà avec l'email : " + email);
    }
}
