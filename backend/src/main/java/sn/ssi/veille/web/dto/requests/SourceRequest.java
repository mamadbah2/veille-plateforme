package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.MethodeCollecte;
import sn.ssi.veille.models.entities.Source.SourceType;

import java.util.Map;

/**
 * DTO pour la création/modification d'une source.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour créer ou modifier une source de veille")
public record SourceRequest(
        // Identité
        @Schema(description = "URL de la source", example = "https://www.reddit.com/r/netsec") String url,

        @Schema(description = "Nom de la source", example = "Reddit NetSec") String nomSource,

        @Schema(description = "Description de la source") String description,

        @Schema(description = "URL du logo") String logoUrl,

        // Collecte
        @Schema(description = "Méthode de collecte", example = "API") MethodeCollecte methodeCollecte,

        @Schema(description = "Source active", example = "true") Boolean active,

        @Schema(description = "Fréquence de scraping en minutes", example = "60") Integer frequenceScraping,

        // Authentification
        @Schema(description = "Clé API si nécessaire") String apiKey,

        @Schema(description = "En-têtes HTTP personnalisés") Map<String, String> headers,

        // Configuration
        @Schema(description = "Langue (FR, EN)", example = "EN") String langue,

        @Schema(description = "Priorité de scraping (1-10)", example = "5") Integer priorite,

        @Schema(description = "ID de la catégorie par défaut") String categorieParDefaut,

        @Schema(description = "Timeout en secondes", example = "30") Integer timeout,

        @Schema(description = "Nombre de tentatives", example = "3") Integer retryCount,

        @Schema(description = "Limite de requêtes par minute", example = "60") Integer rateLimitPerMinute,

        @Schema(description = "Max articles par synchro", example = "100") Integer maxArticlesPerSync,

        // Crédibilité
        @Schema(description = "Score de confiance (1-10)", example = "8") Integer trustScore,

        @Schema(description = "Source vérifiée par admin", example = "true") Boolean verified,

        @Schema(description = "Type de source", example = "MEDIA") SourceType sourceType,

        // Scraping HTML
        @Schema(description = "Sélecteur CSS pour le titre") String selectorTitle,

        @Schema(description = "Sélecteur CSS pour le contenu") String selectorContent,

        @Schema(description = "Sélecteur CSS pour la date") String selectorDate) {
}
