package sn.ssi.veille.exceptions;

/**
 * Exception levée quand une source n'est pas trouvée.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class SourceNotFoundException extends ResourceNotFoundException {

    public SourceNotFoundException(String id) {
        super("Source", "id", id);
    }
}
