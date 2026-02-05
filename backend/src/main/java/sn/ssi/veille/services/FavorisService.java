package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.FavorisRequest;
import sn.ssi.veille.web.dto.requests.UpdateFavorisRequest;
import sn.ssi.veille.web.dto.responses.FavorisResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

/**
 * Interface du service de gestion des favoris.
 * 
 * <p>Ce service permet aux utilisateurs de sauvegarder des articles
 * pour une lecture ultérieure.</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Ajout/suppression de favoris</li>
 *   <li>Gestion des notes et tags personnels</li>
 *   <li>Récupération des favoris</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Vérifier que l'article existe avant ajout</li>
 *   <li>Éviter les doublons (un article ne peut être ajouté qu'une fois)</li>
 *   <li>L'utilisateur ne peut gérer que ses propres favoris</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public interface FavorisService {

    /**
     * Ajoute un article aux favoris de l'utilisateur connecté.
     *
     * @param request les données du favori
     * @return le favori créé
     * @throws sn.ssi.veille.exceptions.ArticleNotFoundException si l'article n'existe pas
     * @throws sn.ssi.veille.exceptions.FavorisAlreadyExistsException si l'article est déjà en favori
     */
    FavorisResponse addToFavoris(FavorisRequest request);

    /**
     * Récupère les favoris de l'utilisateur connecté avec pagination.
     *
     * @param page numéro de la page
     * @param size taille de la page
     * @return une page de favoris
     */
    PageResponse<FavorisResponse> getCurrentUserFavoris(int page, int size);

    /**
     * Récupère un favori par son ID.
     *
     * @param favorisId l'ID du favori
     * @return le favori
     * @throws sn.ssi.veille.exceptions.FavorisNotFoundException si le favori n'existe pas
     * @throws sn.ssi.veille.exceptions.AccessDeniedException si le favori n'appartient pas à l'utilisateur
     */
    FavorisResponse getFavorisById(String favorisId);

    /**
     * Met à jour un favori (note et tags personnels).
     *
     * @param favorisId l'ID du favori
     * @param request les nouvelles données
     * @return le favori mis à jour
     * @throws sn.ssi.veille.exceptions.FavorisNotFoundException si le favori n'existe pas
     */
    FavorisResponse updateFavoris(String favorisId, UpdateFavorisRequest request);

    /**
     * Supprime un favori.
     *
     * @param favorisId l'ID du favori
     * @throws sn.ssi.veille.exceptions.FavorisNotFoundException si le favori n'existe pas
     */
    void removeFavoris(String favorisId);

    /**
     * Supprime un favori par ID d'article.
     *
     * @param articleId l'ID de l'article
     */
    void removeFromFavorisByArticle(String articleId);

    /**
     * Vérifie si un article est en favori.
     *
     * @param articleId l'ID de l'article
     * @return true si l'article est en favori
     */
    boolean isArticleInFavoris(String articleId);

    /**
     * Récupère le nombre de favoris de l'utilisateur.
     *
     * @return le nombre de favoris
     */
    long getFavorisCount();

    /**
     * Recherche les favoris par tag personnel.
     *
     * @param tag le tag à rechercher
     * @param page numéro de la page
     * @param size taille de la page
     * @return une page de favoris
     */
    PageResponse<FavorisResponse> searchFavorisByTag(String tag, int page, int size);
}
