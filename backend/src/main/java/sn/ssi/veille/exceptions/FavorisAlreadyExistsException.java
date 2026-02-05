package sn.ssi.veille.exceptions;

/**
 * Exception levée quand un favori existe déjà.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class FavorisAlreadyExistsException extends ResourceAlreadyExistsException {

    public FavorisAlreadyExistsException(String articleId) {
        super("Cet article est déjà dans vos favoris : " + articleId);
    }
}
