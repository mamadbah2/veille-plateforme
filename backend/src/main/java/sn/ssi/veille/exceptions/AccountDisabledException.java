package sn.ssi.veille.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée quand un compte est désactivé.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountDisabledException extends RuntimeException {

    public AccountDisabledException() {
        super("Ce compte a été désactivé");
    }

    public AccountDisabledException(String message) {
        super(message);
    }
}
