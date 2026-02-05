package sn.ssi.veille.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée quand les credentials sont invalides.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Email/username ou mot de passe incorrect");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
