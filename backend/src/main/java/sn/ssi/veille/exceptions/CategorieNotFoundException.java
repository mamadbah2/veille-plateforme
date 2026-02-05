package sn.ssi.veille.exceptions;

/**
 * Exception levée quand une catégorie n'est pas trouvée.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public class CategorieNotFoundException extends ResourceNotFoundException {

    public CategorieNotFoundException(String id) {
        super("Catégorie", "id", id);
    }
}
