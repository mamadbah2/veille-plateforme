package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

/**
 * Interface du service de gestion des sources.
 * 
 * <p>Ce service gère les opérations CRUD sur les sources de veille
 * (Reddit, NIST, YCombinator, etc.).</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Gestion des sources de données</li>
 *   <li>Activation/désactivation des sources</li>
 *   <li>Configuration des paramètres de scraping</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Vérifier l'unicité des URLs</li>
 *   <li>Valider le format des URLs</li>
 *   <li>Mettre à jour la date de dernière synchronisation</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public interface SourceService {

    /**
     * Crée une nouvelle source.
     *
     * @param request les données de la source
     * @return la source créée
     * @throws sn.ssi.veille.exceptions.SourceAlreadyExistsException si l'URL existe déjà
     */
    SourceResponse createSource(SourceRequest request);

    /**
     * Récupère une source par son ID.
     *
     * @param id l'ID de la source
     * @return la source
     * @throws sn.ssi.veille.exceptions.SourceNotFoundException si la source n'existe pas
     */
    SourceResponse getSourceById(String id);

    /**
     * Récupère toutes les sources.
     *
     * @return la liste de toutes les sources
     */
    List<SourceResponse> getAllSources();

    /**
     * Récupère toutes les sources actives.
     *
     * @return la liste des sources actives
     */
    List<SourceResponse> getActiveSources();

    /**
     * Met à jour une source existante.
     *
     * @param id l'ID de la source
     * @param request les nouvelles données
     * @return la source mise à jour
     * @throws sn.ssi.veille.exceptions.SourceNotFoundException si la source n'existe pas
     */
    SourceResponse updateSource(String id, SourceRequest request);

    /**
     * Active une source.
     *
     * @param id l'ID de la source
     * @return la source activée
     * @throws sn.ssi.veille.exceptions.SourceNotFoundException si la source n'existe pas
     */
    SourceResponse activateSource(String id);

    /**
     * Désactive une source.
     *
     * @param id l'ID de la source
     * @return la source désactivée
     * @throws sn.ssi.veille.exceptions.SourceNotFoundException si la source n'existe pas
     */
    SourceResponse deactivateSource(String id);

    /**
     * Supprime une source.
     * 
     * <p>Attention : cette opération peut affecter les articles liés.</p>
     *
     * @param id l'ID de la source
     * @throws sn.ssi.veille.exceptions.SourceNotFoundException si la source n'existe pas
     */
    void deleteSource(String id);

    /**
     * Met à jour la date de dernière synchronisation.
     * 
     * <p>Appelé par le service de scraping après une collecte réussie.</p>
     *
     * @param id l'ID de la source
     */
    void updateLastSync(String id);
}
