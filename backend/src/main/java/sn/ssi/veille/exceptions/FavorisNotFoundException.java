package sn.ssi.veille.exceptions;

/**
 * Exception levée quand un favori n'est pas trouvé.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class FavorisNotFoundException extends ResourceNotFoundException {

    public FavorisNotFoundException(String id) {
        super("Favori", "id", id);
    }
}
