package sn.ssi.veille.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.ErrorResponse;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.UserResponse;

import java.util.List;

/**
 * Interface du contrôleur de gestion des utilisateurs.
 * 
 * <p>Gère les opérations sur les profils utilisateur.</p>
 * 
 * <h3>Sécurité :</h3>
 * <ul>
 *   <li>Endpoints utilisateur : authentification requise</li>
 *   <li>Endpoints admin : rôle ADMIN requis</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Tag(name = "Utilisateurs", description = "API de gestion des utilisateurs et profils")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/users")
public interface UserController {

    /**
     * Récupère le profil de l'utilisateur connecté.
     *
     * @return les informations du profil
     */
    @Operation(
        summary = "Profil utilisateur",
        description = "Récupère les informations du profil de l'utilisateur connecté."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profil récupéré",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Non authentifié",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/me")
    ResponseEntity<UserResponse> getCurrentUser();

    /**
     * Met à jour le profil de l'utilisateur connecté.
     *
     * @param request les données de mise à jour
     * @return le profil mis à jour
     */
    @Operation(
        summary = "Mise à jour du profil",
        description = "Met à jour le profil de l'utilisateur connecté (username, mot de passe)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profil mis à jour",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Non authentifié",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Username déjà utilisé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/me")
    ResponseEntity<UserResponse> updateCurrentUser(
        @Parameter(description = "Données de mise à jour", required = true)
        @Valid @RequestBody UpdateUserRequest request
    );

    // ==================== ENDPOINTS ADMIN ====================

    /**
     * Récupère tous les utilisateurs (Admin).
     *
     * @return la liste des utilisateurs
     */
    @Operation(
        summary = "[ADMIN] Liste des utilisateurs",
        description = "Récupère la liste de tous les utilisateurs. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Non authentifié",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Accès refusé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping
    ResponseEntity<List<UserResponse>> getAllUsers();

    /**
     * Récupère un utilisateur par ID (Admin).
     *
     * @param id l'ID de l'utilisateur
     * @return les informations de l'utilisateur
     */
    @Operation(
        summary = "[ADMIN] Détails utilisateur",
        description = "Récupère les informations d'un utilisateur par son ID. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Utilisateur trouvé",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Utilisateur non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(
        @Parameter(description = "ID de l'utilisateur", required = true)
        @PathVariable String id
    );

    /**
     * Désactive un utilisateur (Admin).
     *
     * @param id l'ID de l'utilisateur
     * @return message de confirmation
     */
    @Operation(
        summary = "[ADMIN] Désactiver un utilisateur",
        description = "Désactive le compte d'un utilisateur. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Utilisateur désactivé",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Utilisateur non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PatchMapping("/{id}/disable")
    ResponseEntity<MessageResponse> disableUser(
        @Parameter(description = "ID de l'utilisateur", required = true)
        @PathVariable String id
    );

    /**
     * Active un utilisateur (Admin).
     *
     * @param id l'ID de l'utilisateur
     * @return message de confirmation
     */
    @Operation(
        summary = "[ADMIN] Activer un utilisateur",
        description = "Active le compte d'un utilisateur. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Utilisateur activé",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Utilisateur non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PatchMapping("/{id}/enable")
    ResponseEntity<MessageResponse> enableUser(
        @Parameter(description = "ID de l'utilisateur", required = true)
        @PathVariable String id
    );

    /**
     * Supprime un utilisateur (Admin).
     *
     * @param id l'ID de l'utilisateur
     * @return aucun contenu
     */
    @Operation(
        summary = "[ADMIN] Supprimer un utilisateur",
        description = "Supprime définitivement un utilisateur. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Utilisateur supprimé"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Utilisateur non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(
        @Parameter(description = "ID de l'utilisateur", required = true)
        @PathVariable String id
    );
}
