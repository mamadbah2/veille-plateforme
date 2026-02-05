package sn.ssi.veille.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.ssi.veille.web.dto.requests.LoginRequest;
import sn.ssi.veille.web.dto.requests.RefreshTokenRequest;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.responses.AuthResponse;
import sn.ssi.veille.web.dto.responses.ErrorResponse;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.ValidationErrorResponse;

/**
 * Interface du contrôleur d'authentification.
 * 
 * <p>Gère l'inscription, la connexion et la gestion des tokens JWT.</p>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Ces endpoints sont publics (pas d'authentification requise)</li>
 *   <li>Valider les entrées avec @Valid</li>
 *   <li>Retourner les bons codes HTTP</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Tag(name = "Authentification", description = "API d'authentification et gestion des tokens JWT")
@RequestMapping("/api/v1/auth")
public interface AuthController {

    /**
     * Inscrit un nouvel utilisateur.
     *
     * @param request les données d'inscription
     * @return la réponse d'authentification avec tokens JWT
     */
    @Operation(
        summary = "Inscription d'un nouvel utilisateur",
        description = "Crée un nouveau compte utilisateur et retourne les tokens JWT pour l'authentification."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Inscription réussie",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email ou username déjà utilisé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(
        @Parameter(description = "Données d'inscription", required = true)
        @Valid @RequestBody RegisterRequest request
    );

    /**
     * Authentifie un utilisateur.
     *
     * @param request les credentials de connexion
     * @return la réponse d'authentification avec tokens JWT
     */
    @Operation(
        summary = "Connexion utilisateur",
        description = "Authentifie un utilisateur avec son email/username et mot de passe."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Connexion réussie",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Identifiants incorrects",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Compte désactivé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(
        @Parameter(description = "Credentials de connexion", required = true)
        @Valid @RequestBody LoginRequest request
    );

    /**
     * Rafraîchit le token d'accès.
     *
     * @param request le refresh token
     * @return la nouvelle réponse d'authentification
     */
    @Operation(
        summary = "Rafraîchissement du token",
        description = "Génère un nouveau token d'accès à partir du refresh token."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token rafraîchi",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Refresh token invalide ou expiré",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refreshToken(
        @Parameter(description = "Refresh token", required = true)
        @Valid @RequestBody RefreshTokenRequest request
    );

    /**
     * Déconnecte l'utilisateur.
     *
     * @return message de confirmation
     */
    @Operation(
        summary = "Déconnexion",
        description = "Invalide les tokens de l'utilisateur connecté."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Déconnexion réussie",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Non authentifié",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/logout")
    ResponseEntity<MessageResponse> logout();
}
