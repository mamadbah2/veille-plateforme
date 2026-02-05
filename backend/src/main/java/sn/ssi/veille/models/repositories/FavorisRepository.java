package sn.ssi.veille.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.Favoris;

import java.util.List;
import java.util.Optional;

/**
 * Repository MongoDB pour l'entité Favoris.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Repository
public interface FavorisRepository extends MongoRepository<Favoris, String> {

    /**
     * Récupère les favoris d'un utilisateur avec pagination.
     *
     * @param userId l'ID de l'utilisateur
     * @param pageable les paramètres de pagination
     * @return une page de favoris
     */
    Page<Favoris> findByUserId(String userId, Pageable pageable);

    /**
     * Récupère les favoris d'un utilisateur triés par date.
     *
     * @param userId l'ID de l'utilisateur
     * @param pageable les paramètres de pagination
     * @return une page de favoris
     */
    Page<Favoris> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * Récupère tous les favoris d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @return la liste des favoris
     */
    List<Favoris> findByUserId(String userId);

    /**
     * Recherche un favori par utilisateur et article.
     *
     * @param userId l'ID de l'utilisateur
     * @param articleId l'ID de l'article
     * @return un Optional contenant le favori si trouvé
     */
    Optional<Favoris> findByUserIdAndArticleId(String userId, String articleId);

    /**
     * Vérifie si un favori existe pour cet utilisateur et cet article.
     *
     * @param userId l'ID de l'utilisateur
     * @param articleId l'ID de l'article
     * @return true si le favori existe
     */
    boolean existsByUserIdAndArticleId(String userId, String articleId);

    /**
     * Supprime un favori par utilisateur et article.
     *
     * @param userId l'ID de l'utilisateur
     * @param articleId l'ID de l'article
     */
    void deleteByUserIdAndArticleId(String userId, String articleId);

    /**
     * Compte les favoris d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @return le nombre de favoris
     */
    long countByUserId(String userId);

    /**
     * Supprime tous les favoris d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     */
    void deleteByUserId(String userId);

    /**
     * Récupère les favoris contenant un tag personnel spécifique.
     *
     * @param userId l'ID de l'utilisateur
     * @param tag le tag à rechercher
     * @param pageable les paramètres de pagination
     * @return une page de favoris
     */
    Page<Favoris> findByUserIdAndTagsPersonnelsContaining(String userId, String tag, Pageable pageable);
}
