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
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.*;

/**
 * Interface du contrôleur de gestion des articles.
 * 
 * <p>Gère les opérations CRUD et de recherche sur les articles de veille.</p>
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
@Tag(name = "Articles", description = "API de gestion des articles de veille cybersécurité")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/articles")
public interface ArticleController {

    /**
     * Récupère les articles les plus récents.
     *
     * @param page numéro de page (0-indexed)
     * @param size taille de la page
     * @return une page d'articles
     */
    @Operation(
        summary = "Articles récents",
        description = "Récupère les articles les plus récents avec pagination."
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
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getLatestArticles(
        @Parameter(description = "Numéro de page", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Taille de la page", example = "20")
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère un article par son ID.
     *
     * @param id l'ID de l'article
     * @return l'article complet
     */
    @Operation(
        summary = "Détails d'un article",
        description = "Récupère le contenu complet d'un article. Incrémente le compteur de vues."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Article trouvé",
            content = @Content(schema = @Schema(implementation = ArticleResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Article non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    ResponseEntity<ArticleResponse> getArticleById(
        @Parameter(description = "ID de l'article", required = true)
        @PathVariable String id
    );

    /**
     * Recherche des articles selon des critères.
     *
     * @param criteria les critères de recherche
     * @return une page d'articles correspondants
     */
    @Operation(
        summary = "Recherche d'articles",
        description = "Recherche des articles selon différents critères (texte, catégorie, source, gravité, date, tags)."
    )
    @ApiResponse(responseCode = "200", description = "Résultats de recherche")
    @PostMapping("/search")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> searchArticles(
        @Parameter(description = "Critères de recherche", required = true)
        @RequestBody ArticleSearchCriteria criteria
    );

    /**
     * Récupère les articles par catégorie.
     *
     * @param categorieId l'ID de la catégorie
     * @param page numéro de page
     * @param size taille de la page
     * @return une page d'articles
     */
    @Operation(
        summary = "Articles par catégorie",
        description = "Récupère les articles d'une catégorie spécifique."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée"),
        @ApiResponse(
            responseCode = "404",
            description = "Catégorie non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/categorie/{categorieId}")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesByCategorie(
        @Parameter(description = "ID de la catégorie", required = true)
        @PathVariable String categorieId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère les articles par source.
     *
     * @param sourceId l'ID de la source
     * @param page numéro de page
     * @param size taille de la page
     * @return une page d'articles
     */
    @Operation(
        summary = "Articles par source",
        description = "Récupère les articles d'une source spécifique."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée"),
        @ApiResponse(
            responseCode = "404",
            description = "Source non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/source/{sourceId}")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesBySource(
        @Parameter(description = "ID de la source", required = true)
        @PathVariable String sourceId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère les articles par niveau de gravité.
     *
     * @param gravite le niveau de gravité
     * @param page numéro de page
     * @param size taille de la page
     * @return une page d'articles
     */
    @Operation(
        summary = "Articles par gravité",
        description = "Récupère les articles selon leur niveau de gravité."
    )
    @ApiResponse(responseCode = "200", description = "Liste récupérée")
    @GetMapping("/gravite/{gravite}")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesByGravite(
        @Parameter(description = "Niveau de gravité", required = true)
        @PathVariable Gravite gravite,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère les articles tendance (les plus vus).
     *
     * @param page numéro de page
     * @param size taille de la page
     * @return une page d'articles
     */
    @Operation(
        summary = "Articles tendance",
        description = "Récupère les articles les plus populaires (par nombre de vues)."
    )
    @ApiResponse(responseCode = "200", description = "Liste récupérée")
    @GetMapping("/trending")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getTrendingArticles(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère le résumé hebdomadaire.
     *
     * @return le résumé de la semaine
     */
    @Operation(
        summary = "Résumé hebdomadaire",
        description = "Récupère le résumé des articles les plus importants de la semaine."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Résumé généré",
        content = @Content(schema = @Schema(implementation = WeeklySummaryResponse.class))
    )
    @GetMapping("/weekly-summary")
    ResponseEntity<WeeklySummaryResponse> getWeeklySummary();

    // ==================== ENDPOINTS ADMIN ====================

    /**
     * Crée un nouvel article (Admin).
     *
     * @param request les données de l'article
     * @return l'article créé
     */
    @Operation(
        summary = "[ADMIN] Créer un article",
        description = "Crée manuellement un nouvel article. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Article créé",
            content = @Content(schema = @Schema(implementation = ArticleResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données invalides",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Article déjà existant",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    ResponseEntity<ArticleResponse> createArticle(
        @Parameter(description = "Données de l'article", required = true)
        @Valid @RequestBody ArticleRequest request
    );

    /**
     * Met à jour un article (Admin).
     *
     * @param id l'ID de l'article
     * @param request les nouvelles données
     * @return l'article mis à jour
     */
    @Operation(
        summary = "[ADMIN] Modifier un article",
        description = "Met à jour un article existant. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Article mis à jour",
            content = @Content(schema = @Schema(implementation = ArticleResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Article non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    ResponseEntity<ArticleResponse> updateArticle(
        @Parameter(description = "ID de l'article", required = true)
        @PathVariable String id,
        @Parameter(description = "Nouvelles données", required = true)
        @Valid @RequestBody ArticleRequest request
    );

    /**
     * Supprime un article (Admin).
     *
     * @param id l'ID de l'article
     * @return aucun contenu
     */
    @Operation(
        summary = "[ADMIN] Supprimer un article",
        description = "Supprime définitivement un article. Requiert le rôle ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Article supprimé"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Article non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteArticle(
        @Parameter(description = "ID de l'article", required = true)
        @PathVariable String id
    );

    /**
     * Génère un résumé IA pour un article (Bonus).
     *
     * @param id l'ID de l'article
     * @param apiKey la clé API du LLM (optionnelle)
     * @return le résumé généré
     */
    @Operation(
        summary = "Générer résumé IA",
        description = "Génère un résumé de l'article via IA. Peut nécessiter une clé API."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Résumé généré",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Article non trouvé",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping("/{id}/summarize")
    ResponseEntity<MessageResponse> generateAISummary(
        @Parameter(description = "ID de l'article", required = true)
        @PathVariable String id,
        @Parameter(description = "Clé API du LLM (optionnelle)")
        @RequestParam(required = false) String apiKey
    );
}
