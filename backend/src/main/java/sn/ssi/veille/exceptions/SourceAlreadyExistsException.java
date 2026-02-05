package sn.ssi.veille.exceptions;

/**
 * Exception levée quand une source existe déjà.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class SourceAlreadyExistsException extends ResourceAlreadyExistsException {

    public SourceAlreadyExistsException(String url) {
        super("Une source existe déjà avec cette URL : " + url);
    }
}
