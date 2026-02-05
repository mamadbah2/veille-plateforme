package sn.ssi.veille.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée quand un token JWT est invalide ou expiré.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Token invalide ou expiré");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
