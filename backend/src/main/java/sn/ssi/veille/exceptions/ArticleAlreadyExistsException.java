package sn.ssi.veille.exceptions;

/**
 * Exception levée quand un article existe déjà (même URL + source).
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class ArticleAlreadyExistsException extends ResourceAlreadyExistsException {

    public ArticleAlreadyExistsException(String url) {
        super("Un article existe déjà avec cette URL : " + url);
    }
}
