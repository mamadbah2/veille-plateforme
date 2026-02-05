package sn.ssi.veille.exceptions;

/**
 * Exception levée quand une catégorie existe déjà.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class CategorieAlreadyExistsException extends ResourceAlreadyExistsException {

    public CategorieAlreadyExistsException(String nom) {
        super("Une catégorie existe déjà avec ce nom : " + nom);
    }
}
