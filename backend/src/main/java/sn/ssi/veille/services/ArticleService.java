package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.ArticleResponse;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;
import sn.ssi.veille.web.dto.responses.WeeklySummaryResponse;

/**
 * Interface du service de gestion des articles.
 * 
 * <p>Ce service gère les opérations CRUD sur les articles de veille
 * ainsi que les fonctionnalités de recherche et de résumé.</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Gestion des articles</li>
 *   <li>Recherche et filtrage</li>
 *   <li>Génération de résumés hebdomadaires</li>
 *   <li>Incrémentation des vues</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Vérifier l'existence de la source et de la catégorie</li>
 *   <li>Gérer l'idempotence (éviter les doublons par URL+source)</li>
 *   <li>Utiliser les index MongoDB pour les recherches</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public interface ArticleService {

    /**
     * Crée un nouvel article.
     * 
     * <p>Vérifie qu'un article avec la même URL et source n'existe pas déjà.</p>
     *
     * @param request les données de l'article
     * @return l'article créé
     * @throws sn.ssi.veille.exceptions.ArticleAlreadyExistsException si l'article existe déjà
     * @throws sn.ssi.veille.exceptions.SourceNotFoundException si la source n'existe pas
     * @throws sn.ssi.veille.exceptions.CategorieNotFoundException si la catégorie n'existe pas
     */
    ArticleResponse createArticle(ArticleRequest request);

    /**
     * Récupère un article par son ID.
     * 
     * <p>Incrémente le compteur de vues.</p>
     *
     * @param id l'ID de l'article
     * @return l'article complet
     * @throws sn.ssi.veille.exceptions.ArticleNotFoundException si l'article n'existe pas
     */
    ArticleResponse getArticleById(String id);

    /**
     * Récupère les articles les plus récents avec pagination.
     *
     * @param page numéro de la page (0-indexed)
     * @param size taille de la page
     * @return une page d'articles résumés
     */
    PageResponse<ArticleSummaryResponse> getLatestArticles(int page, int size);

    /**
     * Recherche des articles selon des critères multiples.
     * 
     * <p>Supporte la recherche textuelle, le filtrage par catégorie,
     * source, gravité, date, tags, etc.</p>
     *
     * @param criteria les critères de recherche
     * @return une page d'articles résumés
     */
    PageResponse<ArticleSummaryResponse> searchArticles(ArticleSearchCriteria criteria);

    /**
     * Récupère les articles par catégorie.
     *
     * @param categorieId l'ID de la catégorie
     * @param page numéro de la page
     * @param size taille de la page
     * @return une page d'articles résumés
     */
    PageResponse<ArticleSummaryResponse> getArticlesByCategorie(String categorieId, int page, int size);

    /**
     * Récupère les articles par source.
     *
     * @param sourceId l'ID de la source
     * @param page numéro de la page
     * @param size taille de la page
     * @return une page d'articles résumés
     */
    PageResponse<ArticleSummaryResponse> getArticlesBySource(String sourceId, int page, int size);

    /**
     * Récupère les articles les plus populaires (vues).
     *
     * @param page numéro de la page
     * @param size taille de la page
     * @return une page d'articles résumés
     */
    PageResponse<ArticleSummaryResponse> getTrendingArticles(int page, int size);

    /**
     * Met à jour un article existant.
     *
     * @param id l'ID de l'article
     * @param request les nouvelles données
     * @return l'article mis à jour
     * @throws sn.ssi.veille.exceptions.ArticleNotFoundException si l'article n'existe pas
     */
    ArticleResponse updateArticle(String id, ArticleRequest request);

    /**
     * Supprime un article.
     *
     * @param id l'ID de l'article
     * @throws sn.ssi.veille.exceptions.ArticleNotFoundException si l'article n'existe pas
     */
    void deleteArticle(String id);

    /**
     * Génère le résumé hebdomadaire.
     * 
     * <p>Ce résumé contient :</p>
     * <ul>
     *   <li>Les articles les plus importants de la semaine</li>
     *   <li>Les statistiques par catégorie et gravité</li>
     *   <li>Optionnel : un résumé généré par IA</li>
     * </ul>
     *
     * @return le résumé hebdomadaire
     */
    WeeklySummaryResponse getWeeklySummary();

    /**
     * Génère un résumé par IA pour un article.
     * 
     * <p>Bonus : utilise un LLM externe (clé API fournie par l'utilisateur).</p>
     *
     * @param articleId l'ID de l'article
     * @param apiKey la clé API du LLM (optionnelle, utilise la config sinon)
     * @return le résumé généré
     */
    String generateAISummary(String articleId, String apiKey);

    /**
     * Vérifie si un article existe déjà (par URL et source).
     * 
     * <p>Utilisé par le service de scraping pour éviter les doublons.</p>
     *
     * @param urlOrigine l'URL de l'article
     * @param sourceId l'ID de la source
     * @return true si l'article existe déjà
     */
    boolean articleExists(String urlOrigine, String sourceId);
}
