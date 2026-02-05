package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Categorie;

import java.util.Optional;

/**
 * Repository MongoDB pour l'entité Categorie.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Repository
public interface CategorieRepository extends MongoRepository<Categorie, String> {

    /**
     * Recherche une catégorie par son nom.
     *
     * @param nomCategorie le nom de la catégorie
     * @return un Optional contenant la catégorie si trouvée
     */
    Optional<Categorie> findByNomCategorie(String nomCategorie);

    /**
     * Vérifie si une catégorie existe avec ce nom.
     *
     * @param nomCategorie le nom à vérifier
     * @return true si le nom existe déjà
     */
    boolean existsByNomCategorie(String nomCategorie);
}
