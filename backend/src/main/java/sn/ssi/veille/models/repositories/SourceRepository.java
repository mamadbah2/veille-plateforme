package sn.ssi.veille.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.MethodeCollecte;
import sn.ssi.veille.models.entities.Source;

import java.util.List;
import java.util.Optional;

/**
 * Repository MongoDB pour l'entité Source.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Repository
public interface SourceRepository extends MongoRepository<Source, String> {

    /**
     * Recherche une source par son URL.
     *
     * @param url l'URL de la source
     * @return un Optional contenant la source si trouvée
     */
    Optional<Source> findByUrl(String url);

    /**
     * Recherche une source par son nom.
     *
     * @param nomSource le nom de la source
     * @return un Optional contenant la source si trouvée
     */
    Optional<Source> findByNomSource(String nomSource);

    /**
     * Récupère toutes les sources actives.
     *
     * @return la liste des sources actives
     */
    List<Source> findByActiveTrue();

    /**
     * Récupère toutes les sources par méthode de collecte.
     *
     * @param methodeCollecte la méthode de collecte
     * @return la liste des sources correspondantes
     */
    List<Source> findByMethodeCollecte(MethodeCollecte methodeCollecte);

    /**
     * Récupère les sources actives par méthode de collecte.
     *
     * @param methodeCollecte la méthode de collecte
     * @return la liste des sources actives correspondantes
     */
    List<Source> findByActiveTrueAndMethodeCollecte(MethodeCollecte methodeCollecte);

    /**
     * Vérifie si une source existe avec cette URL.
     *
     * @param url l'URL à vérifier
     * @return true si l'URL existe déjà
     */
    boolean existsByUrl(String url);
}
