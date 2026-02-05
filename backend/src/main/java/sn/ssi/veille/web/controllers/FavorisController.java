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
import sn.ssi.veille.web.dto.requests.FavorisRequest;
import sn.ssi.veille.web.dto.requests.UpdateFavorisRequest;
import sn.ssi.veille.web.dto.responses.ErrorResponse;
import sn.ssi.veille.web.dto.responses.FavorisResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

/**
 * Interface du contrôleur de gestion des favoris.
 * 
 * <p>Permet aux utilisateurs de gérer leurs articles favoris.</p>
 * 
 * <h3>Sécurité :</h3>
 * <ul>
 *   <li>Tous les endpoints nécessitent une authentification</li>
 *   <li>L'utilisateur ne peut gérer que ses propres favoris</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Tag(name = "Favoris", description = "API de gestion des articles favoris")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/favoris")
public interface FavorisController {

    /**
     * Récupère les favoris de l'utilisateur connecté.
     *
     * @param page numéro de page
     * @param size taille de la page
     * @return une page de favoris
     */
    @Operation(
        summary = "Mes favoris",
        description = "Récupère la liste des articles favoris de l'utilisateur connecté."
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
    ResponseEntity<PageResponse<FavorisResponse>> getMyFavoris(
        @Parameter(description = "Numéro de page", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Taille de la page", example = "20")
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère un favori par son ID.
     *
     * @param id l'ID du favori
     * @return le favori
     */
    @Operation(
        summary = "Détails d'un favori",
        description = "Récupère les détails d'un favori spécifique."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Favori trouvé",
            content = @Content(schema = @Schema(implementation = FavorisResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Favori non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<FavorisResponse> getFavorisById(
        @Parameter(description = "ID du favori", required = true)
        @PathVariable String id
    );

    /**
     * Vérifie si un article est en favori.
     *
     * @param articleId l'ID de l'article
     * @return true si l'article est en favori
     */
    @Operation(
        summary = "Vérifier si favori",
        description = "Vérifie si un article est dans les favoris de l'utilisateur."
    )
    @ApiResponse(responseCode = "200", description = "Statut récupéré")
    @GetMapping("/check/{articleId}")
    ResponseEntity<Boolean> isArticleInFavoris(
        @Parameter(description = "ID de l'article", required = true)
        @PathVariable String articleId
    );

    /**
     * Récupère le nombre de favoris.
     *
     * @return le nombre de favoris
     */
    @Operation(
        summary = "Compteur de favoris",
        description = "Récupère le nombre total de favoris de l'utilisateur."
    )
    @ApiResponse(responseCode = "200", description = "Compteur récupéré")
    @GetMapping("/count")
    ResponseEntity<Long> getFavorisCount();

    /**
     * Recherche des favoris par tag personnel.
     *
     * @param tag le tag à rechercher
     * @param page numéro de page
     * @param size taille de la page
     * @return une page de favoris
     */
    @Operation(
        summary = "Recherche par tag",
        description = "Recherche les favoris contenant un tag personnel spécifique."
    )
    @ApiResponse(responseCode = "200", description = "Résultats récupérés")
    @GetMapping("/search")
    ResponseEntity<PageResponse<FavorisResponse>> searchFavorisByTag(
        @Parameter(description = "Tag à rechercher", required = true)
        @RequestParam String tag,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Ajoute un article aux favoris.
     *
     * @param request les données du favori
     * @return le favori créé
     */
    @Operation(
        summary = "Ajouter aux favoris",
        description = "Ajoute un article aux favoris avec une note et des tags personnels optionnels."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Favori créé",
            content = @Content(schema = @Schema(implementation = FavorisResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Article non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Article déjà en favori",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<FavorisResponse> addToFavoris(
        @Parameter(description = "Données du favori", required = true)
        @Valid @RequestBody FavorisRequest request
    );

    /**
     * Met à jour un favori.
     *
     * @param id l'ID du favori
     * @param request les nouvelles données
     * @return le favori mis à jour
     */
    @Operation(
        summary = "Modifier un favori",
        description = "Met à jour la note et les tags personnels d'un favori."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Favori mis à jour",
            content = @Content(schema = @Schema(implementation = FavorisResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Favori non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<FavorisResponse> updateFavoris(
        @Parameter(description = "ID du favori", required = true)
        @PathVariable String id,
        @Parameter(description = "Nouvelles données", required = true)
        @Valid @RequestBody UpdateFavorisRequest request
    );

    /**
     * Supprime un favori par son ID.
     *
     * @param id l'ID du favori
     * @return aucun contenu
     */
    @Operation(
        summary = "Supprimer un favori",
        description = "Retire un article des favoris."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Favori supprimé"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Favori non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> removeFavoris(
        @Parameter(description = "ID du favori", required = true)
        @PathVariable String id
    );

    /**
     * Supprime un favori par ID d'article.
     *
     * @param articleId l'ID de l'article
     * @return aucun contenu
     */
    @Operation(
        summary = "Retirer des favoris par article",
        description = "Retire un article des favoris en utilisant l'ID de l'article."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Favori supprimé"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Article non trouvé dans les favoris",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/article/{articleId}")
    ResponseEntity<Void> removeFavorisByArticle(
        @Parameter(description = "ID de l'article", required = true)
        @PathVariable String articleId
    );
}
