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
import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;
import sn.ssi.veille.web.dto.responses.ErrorResponse;

import java.util.List;

/**
 * Interface du contrôleur de gestion des catégories.
 * 
 * <p>Gère les opérations CRUD sur les catégories d'articles.</p>
 * 
 * <h3>Sécurité :</h3>
 * <ul>
 *   <li>Lecture : tous les utilisateurs authentifiés</li>
 *   <li>Écriture : rôle ADMIN requis</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Tag(name = "Catégories", description = "API de gestion des catégories d'articles")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/categories")
public interface CategorieController {

    /**
     * Récupère toutes les catégories.
     *
     * @return la liste des catégories
     */
    @Operation(
        summary = "Liste des catégories",
        description = "Récupère la liste de toutes les catégories disponibles."
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
        )
    })
    @GetMapping
    ResponseEntity<List<CategorieResponse>> getAllCategories();

    /**
     * Récupère une catégorie par son ID.
     *
     * @param id l'ID de la catégorie
     * @return la catégorie
     */
    @Operation(
        summary = "Détails d'une catégorie",
        description = "Récupère les informations d'une catégorie par son ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Catégorie trouvée",
            content = @Content(schema = @Schema(implementation = CategorieResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Catégorie non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<CategorieResponse> getCategorieById(
        @Parameter(description = "ID de la catégorie", required = true)
        @PathVariable String id
    );

    // ==================== ENDPOINTS ADMIN ====================

    /**
     * Crée une nouvelle catégorie (Admin).
     *
     * @param request les données de la catégorie
     * @return la catégorie créée
     */
    @Operation(
        summary = "[ADMIN] Créer une catégorie",
        description = "Crée une nouvelle catégorie. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Catégorie créée",
            content = @Content(schema = @Schema(implementation = CategorieResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Nom déjà utilisé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<CategorieResponse> createCategorie(
        @Parameter(description = "Données de la catégorie", required = true)
        @Valid @RequestBody CategorieRequest request
    );

    /**
     * Met à jour une catégorie (Admin).
     *
     * @param id l'ID de la catégorie
     * @param request les nouvelles données
     * @return la catégorie mise à jour
     */
    @Operation(
        summary = "[ADMIN] Modifier une catégorie",
        description = "Met à jour une catégorie existante. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Catégorie mise à jour",
            content = @Content(schema = @Schema(implementation = CategorieResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Catégorie non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<CategorieResponse> updateCategorie(
        @Parameter(description = "ID de la catégorie", required = true)
        @PathVariable String id,
        @Parameter(description = "Nouvelles données", required = true)
        @Valid @RequestBody CategorieRequest request
    );

    /**
     * Supprime une catégorie (Admin).
     *
     * @param id l'ID de la catégorie
     * @return aucun contenu
     */
    @Operation(
        summary = "[ADMIN] Supprimer une catégorie",
        description = "Supprime une catégorie. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Catégorie supprimée"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Catégorie non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCategorie(
        @Parameter(description = "ID de la catégorie", required = true)
        @PathVariable String id
    );
}
