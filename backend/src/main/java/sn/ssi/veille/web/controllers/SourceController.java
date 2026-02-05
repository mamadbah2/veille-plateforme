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
import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.ErrorResponse;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

/**
 * Interface du contrôleur de gestion des sources.
 * 
 * <p>Gère les opérations CRUD sur les sources de veille (Reddit, NIST, etc.).</p>
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
@Tag(name = "Sources", description = "API de gestion des sources de veille")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/sources")
public interface SourceController {

    /**
     * Récupère toutes les sources.
     *
     * @return la liste des sources
     */
    @Operation(
        summary = "Liste des sources",
        description = "Récupère la liste de toutes les sources de veille."
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
    ResponseEntity<List<SourceResponse>> getAllSources();

    /**
     * Récupère les sources actives.
     *
     * @return la liste des sources actives
     */
    @Operation(
        summary = "Sources actives",
        description = "Récupère la liste des sources actuellement actives."
    )
    @ApiResponse(responseCode = "200", description = "Liste récupérée")
    @GetMapping("/active")
    ResponseEntity<List<SourceResponse>> getActiveSources();

    /**
     * Récupère une source par son ID.
     *
     * @param id l'ID de la source
     * @return la source
     */
    @Operation(
        summary = "Détails d'une source",
        description = "Récupère les informations détaillées d'une source."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Source trouvée",
            content = @Content(schema = @Schema(implementation = SourceResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Source non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<SourceResponse> getSourceById(
        @Parameter(description = "ID de la source", required = true)
        @PathVariable String id
    );

    // ==================== ENDPOINTS ADMIN ====================

    /**
     * Crée une nouvelle source (Admin).
     *
     * @param request les données de la source
     * @return la source créée
     */
    @Operation(
        summary = "[ADMIN] Créer une source",
        description = "Crée une nouvelle source de veille. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Source créée",
            content = @Content(schema = @Schema(implementation = SourceResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "URL déjà utilisée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<SourceResponse> createSource(
        @Parameter(description = "Données de la source", required = true)
        @Valid @RequestBody SourceRequest request
    );

    /**
     * Met à jour une source (Admin).
     *
     * @param id l'ID de la source
     * @param request les nouvelles données
     * @return la source mise à jour
     */
    @Operation(
        summary = "[ADMIN] Modifier une source",
        description = "Met à jour une source existante. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Source mise à jour",
            content = @Content(schema = @Schema(implementation = SourceResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Source non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<SourceResponse> updateSource(
        @Parameter(description = "ID de la source", required = true)
        @PathVariable String id,
        @Parameter(description = "Nouvelles données", required = true)
        @Valid @RequestBody SourceRequest request
    );

    /**
     * Active une source (Admin).
     *
     * @param id l'ID de la source
     * @return la source activée
     */
    @Operation(
        summary = "[ADMIN] Activer une source",
        description = "Active une source pour le scraping. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Source activée",
            content = @Content(schema = @Schema(implementation = SourceResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Source non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PatchMapping("/{id}/activate")
    ResponseEntity<SourceResponse> activateSource(
        @Parameter(description = "ID de la source", required = true)
        @PathVariable String id
    );

    /**
     * Désactive une source (Admin).
     *
     * @param id l'ID de la source
     * @return la source désactivée
     */
    @Operation(
        summary = "[ADMIN] Désactiver une source",
        description = "Désactive une source. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Source désactivée",
            content = @Content(schema = @Schema(implementation = SourceResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Source non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PatchMapping("/{id}/deactivate")
    ResponseEntity<SourceResponse> deactivateSource(
        @Parameter(description = "ID de la source", required = true)
        @PathVariable String id
    );

    /**
     * Supprime une source (Admin).
     *
     * @param id l'ID de la source
     * @return aucun contenu
     */
    @Operation(
        summary = "[ADMIN] Supprimer une source",
        description = "Supprime définitivement une source. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Source supprimée"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Source non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSource(
        @Parameter(description = "ID de la source", required = true)
        @PathVariable String id
    );

    /**
     * Déclenche le scraping d'une source (Admin).
     *
     * @param id l'ID de la source
     * @return message avec le nombre d'articles collectés
     */
    @Operation(
        summary = "[ADMIN] Scraper une source",
        description = "Déclenche manuellement le scraping d'une source. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Scraping terminé",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Source non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erreur de scraping",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/{id}/scrape")
    ResponseEntity<MessageResponse> triggerScraping(
        @Parameter(description = "ID de la source", required = true)
        @PathVariable String id
    );
}
