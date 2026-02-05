package sn.ssi.veille.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception de base pour les ressources déjà existantes.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s existe déjà avec %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
