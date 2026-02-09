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

    Optional<Categorie> findByNomCategorie(String nomCategorie);
}
