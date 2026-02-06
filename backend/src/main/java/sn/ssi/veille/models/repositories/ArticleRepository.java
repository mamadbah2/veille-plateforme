package sn.ssi.veille.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Gravite;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository MongoDB pour l'entité Article.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

    /**
     * Recherche un article par son URL d'origine et sa source.
     * Utilisé pour vérifier les doublons lors du scraping.
     *
     * @param urlOrigine l'URL originale de l'article
     * @param sourceId l'ID de la source
     * @return un Optional contenant l'article si trouvé
     */
    Optional<Article> findByUrlOrigineAndSourceId(String urlOrigine, String sourceId);

    /**
     * Récupère les articles d'une source avec pagination.
     *
     * @param sourceId l'ID de la source
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findBySourceId(String sourceId, Pageable pageable);

    /**
     * Récupère les articles d'une catégorie avec pagination.
     *
     * @param categorieId l'ID de la catégorie
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findByCategorieId(String categorieId, Pageable pageable);

    /**
     * Récupère les articles par niveau de gravité avec pagination.
     *
     * @param gravite le niveau de gravité
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findByGravite(Gravite gravite, Pageable pageable);

    /**
     * Récupère les articles publiés entre deux dates avec pagination.
     *
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findByDatePublicationBetween(LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable);

    /**
     * Récupère les articles publiés après une date avec pagination.
     *
     * @param date la date limite
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findByDatePublicationAfter(LocalDateTime date, Pageable pageable);

    /**
     * Recherche textuelle dans les articles.
     *
     * @param searchText le texte à rechercher
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    @Query("{ $text: { $search: ?0 } }")
    Page<Article> searchByText(String searchText, Pageable pageable);

    /**
     * Récupère les articles les plus récents.
     *
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findAllByOrderByDatePublicationDesc(Pageable pageable);

    /**
     * Récupère les articles les plus vus.
     *
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findAllByOrderByNombreVuesDesc(Pageable pageable);

    /**
     * Compte les articles par catégorie.
     *
     * @param categorieId l'ID de la catégorie
     * @return le nombre d'articles
     */
    long countByCategorieId(String categorieId);

    /**
     * Compte les articles par source.
     *
     * @param sourceId l'ID de la source
     * @return le nombre d'articles
     */
    long countBySourceId(String sourceId);

    /**
     * Récupère les articles de la semaine par niveau de gravité.
     *
     * @param dateDebut la date de début de la semaine
     * @param dateFin la date de fin de la semaine
     * @param gravite le niveau de gravité minimum
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findByDatePublicationBetweenAndGravite(
            LocalDateTime dateDebut, 
            LocalDateTime dateFin, 
            Gravite gravite, 
            Pageable pageable);

    /**
     * Vérifie si un article existe avec cette URL et cette source.
     *
     * @param urlOrigine l'URL originale
     * @param sourceId l'ID de la source
     * @return true si l'article existe
     */
    boolean existsByUrlOrigineAndSourceId(String urlOrigine, String sourceId);

    /**
     * Récupère les articles contenant un tag spécifique.
     *
     * @param tag le tag à rechercher
     * @param pageable les paramètres de pagination
     * @return une page d'articles
     */
    Page<Article> findByTagsContaining(String tag, Pageable pageable);

    /**
     * Compte les articles publiés entre deux dates.
     *
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @return le nombre d'articles
     */
    long countByDatePublicationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
}
