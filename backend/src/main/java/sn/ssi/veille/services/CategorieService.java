package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;

/**
 * Interface du service de gestion des catégories.
 * 
 * <p>Ce service gère les opérations CRUD sur les catégories d'articles.</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Gestion des catégories</li>
 *   <li>Classification des articles</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Vérifier l'unicité des noms de catégories</li>
 *   <li>Valider le format des couleurs HEX</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public interface CategorieService {

    /**
     * Crée une nouvelle catégorie.
     *
     * @param request les données de la catégorie
     * @return la catégorie créée
     * @throws sn.ssi.veille.exceptions.CategorieAlreadyExistsException si le nom existe déjà
     */
    CategorieResponse createCategorie(CategorieRequest request);

    /**
     * Récupère une catégorie par son ID.
     *
     * @param id l'ID de la catégorie
     * @return la catégorie
     * @throws sn.ssi.veille.exceptions.CategorieNotFoundException si la catégorie n'existe pas
     */
    CategorieResponse getCategorieById(String id);

    /**
     * Récupère une catégorie par son nom.
     *
     * @param nom le nom de la catégorie
     * @return la catégorie
     * @throws sn.ssi.veille.exceptions.CategorieNotFoundException si la catégorie n'existe pas
     */
    CategorieResponse getCategorieByNom(String nom);

    /**
     * Récupère toutes les catégories.
     *
     * @return la liste de toutes les catégories
     */
    List<CategorieResponse> getAllCategories();

    /**
     * Met à jour une catégorie existante.
     *
     * @param id l'ID de la catégorie
     * @param request les nouvelles données
     * @return la catégorie mise à jour
     * @throws sn.ssi.veille.exceptions.CategorieNotFoundException si la catégorie n'existe pas
     * @throws sn.ssi.veille.exceptions.CategorieAlreadyExistsException si le nouveau nom existe déjà
     */
    CategorieResponse updateCategorie(String id, CategorieRequest request);

    /**
     * Supprime une catégorie.
     * 
     * <p>Attention : cette opération peut affecter les articles liés.
     * Il est recommandé de réassigner les articles à une catégorie par défaut.</p>
     *
     * @param id l'ID de la catégorie
     * @throws sn.ssi.veille.exceptions.CategorieNotFoundException si la catégorie n'existe pas
     */
    void deleteCategorie(String id);
}
