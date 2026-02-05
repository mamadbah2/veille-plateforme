package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;

import java.util.List;

/**
 * Interface du service de gestion des utilisateurs.
 * 
 * <p>Ce service gère les opérations CRUD sur les utilisateurs
 * et les fonctionnalités liées au profil.</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Récupération des informations utilisateur</li>
 *   <li>Mise à jour du profil</li>
 *   <li>Gestion des comptes (activation/désactivation)</li>
 *   <li>Administration des utilisateurs</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Vérifier les permissions avant modification</li>
 *   <li>Ne jamais exposer le mot de passe hashé</li>
 *   <li>Utiliser le mapper pour les conversions</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public interface UserService {

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id l'ID de l'utilisateur
     * @return les informations de l'utilisateur
     * @throws sn.ssi.veille.exceptions.UserNotFoundException si l'utilisateur n'existe pas
     */
    UserResponse getUserById(String id);

    /**
     * Récupère un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur
     * @return les informations de l'utilisateur
     * @throws sn.ssi.veille.exceptions.UserNotFoundException si l'utilisateur n'existe pas
     */
    UserResponse getUserByEmail(String email);

    /**
     * Récupère le profil de l'utilisateur connecté.
     * 
     * <p>L'ID de l'utilisateur doit être extrait du contexte de sécurité.</p>
     *
     * @return les informations de l'utilisateur connecté
     */
    UserResponse getCurrentUser();

    /**
     * Met à jour le profil de l'utilisateur connecté.
     * 
     * <p>Étapes à implémenter :</p>
     * <ol>
     *   <li>Vérifier que le nouveau username n'est pas déjà pris</li>
     *   <li>Si changement de mot de passe, vérifier l'ancien mot de passe</li>
     *   <li>Hasher le nouveau mot de passe si fourni</li>
     *   <li>Mettre à jour l'entité</li>
     * </ol>
     *
     * @param request les données de mise à jour
     * @return les informations mises à jour
     * @throws sn.ssi.veille.exceptions.UsernameAlreadyExistsException si le username existe déjà
     * @throws sn.ssi.veille.exceptions.InvalidCredentialsException si l'ancien mot de passe est incorrect
     */
    UserResponse updateCurrentUser(UpdateUserRequest request);

    /**
     * Récupère tous les utilisateurs (Admin uniquement).
     *
     * @return la liste de tous les utilisateurs
     */
    List<UserResponse> getAllUsers();

    /**
     * Désactive un compte utilisateur (Admin uniquement).
     *
     * @param userId l'ID de l'utilisateur à désactiver
     * @throws sn.ssi.veille.exceptions.UserNotFoundException si l'utilisateur n'existe pas
     */
    void disableUser(String userId);

    /**
     * Active un compte utilisateur (Admin uniquement).
     *
     * @param userId l'ID de l'utilisateur à activer
     * @throws sn.ssi.veille.exceptions.UserNotFoundException si l'utilisateur n'existe pas
     */
    void enableUser(String userId);

    /**
     * Supprime un compte utilisateur (Admin uniquement).
     * 
     * <p>Cette opération doit également supprimer les favoris et notifications associés.</p>
     *
     * @param userId l'ID de l'utilisateur à supprimer
     * @throws sn.ssi.veille.exceptions.UserNotFoundException si l'utilisateur n'existe pas
     */
    void deleteUser(String userId);
}
