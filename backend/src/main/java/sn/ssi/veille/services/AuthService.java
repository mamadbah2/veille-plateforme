package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.LoginRequest;
import sn.ssi.veille.web.dto.requests.RefreshTokenRequest;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.responses.AuthResponse;

/**
 * Interface du service d'authentification.
 * 
 * <p>Ce service gère l'inscription, la connexion et la gestion des tokens JWT.</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Inscription des nouveaux utilisateurs</li>
 *   <li>Authentification et génération de tokens JWT</li>
 *   <li>Rafraîchissement des tokens</li>
 *   <li>Validation des tokens</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Utiliser BCryptPasswordEncoder pour hasher les mots de passe</li>
 *   <li>Générer des tokens JWT avec une expiration appropriée</li>
 *   <li>Implémenter la logique de refresh token</li>
 *   <li>Gérer les exceptions personnalisées (EmailExistsException, etc.)</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 * @see sn.ssi.veille.models.entities.User
 */
public interface AuthService {

    /**
     * Inscrit un nouvel utilisateur.
     * 
     * <p>Étapes à implémenter :</p>
     * <ol>
     *   <li>Vérifier que l'email n'existe pas déjà</li>
     *   <li>Vérifier que le username n'existe pas déjà</li>
     *   <li>Hasher le mot de passe avec BCrypt</li>
     *   <li>Créer l'utilisateur avec le rôle ROLE_USER par défaut</li>
     *   <li>Générer et retourner les tokens JWT</li>
     * </ol>
     *
     * @param request les données d'inscription
     * @return la réponse d'authentification avec les tokens
     * @throws sn.ssi.veille.exceptions.EmailAlreadyExistsException si l'email existe déjà
     * @throws sn.ssi.veille.exceptions.UsernameAlreadyExistsException si le username existe déjà
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authentifie un utilisateur.
     * 
     * <p>Étapes à implémenter :</p>
     * <ol>
     *   <li>Rechercher l'utilisateur par email ou username</li>
     *   <li>Vérifier le mot de passe</li>
     *   <li>Vérifier que le compte est actif</li>
     *   <li>Générer et retourner les tokens JWT</li>
     * </ol>
     *
     * @param request les credentials de connexion
     * @return la réponse d'authentification avec les tokens
     * @throws sn.ssi.veille.exceptions.InvalidCredentialsException si les credentials sont invalides
     * @throws sn.ssi.veille.exceptions.AccountDisabledException si le compte est désactivé
     */
    AuthResponse login(LoginRequest request);

    /**
     * Rafraîchit le token d'accès.
     * 
     * <p>Étapes à implémenter :</p>
     * <ol>
     *   <li>Valider le refresh token</li>
     *   <li>Extraire l'utilisateur du token</li>
     *   <li>Générer un nouveau access token</li>
     *   <li>Optionnel : générer un nouveau refresh token (rotation)</li>
     * </ol>
     *
     * @param request le refresh token
     * @return la réponse d'authentification avec les nouveaux tokens
     * @throws sn.ssi.veille.exceptions.InvalidTokenException si le token est invalide ou expiré
     */
    AuthResponse refreshToken(RefreshTokenRequest request);

    /**
     * Invalide tous les tokens d'un utilisateur (déconnexion).
     * 
     * <p>Implémentation suggérée : utiliser une blacklist de tokens ou 
     * incrémenter un compteur de version dans le User.</p>
     *
     * @param userId l'ID de l'utilisateur
     */
    void logout(String userId);

    /**
     * Vérifie si un token est valide.
     *
     * @param token le token à vérifier
     * @return true si le token est valide
     */
    boolean isTokenValid(String token);
}
