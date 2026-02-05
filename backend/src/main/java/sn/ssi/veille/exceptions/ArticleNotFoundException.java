package sn.ssi.veille.exceptions;

/**
 * Exception levée quand un article n'est pas trouvé.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class ArticleNotFoundException extends ResourceNotFoundException {

    public ArticleNotFoundException(String id) {
        super("Article", "id", id);
    }
}
