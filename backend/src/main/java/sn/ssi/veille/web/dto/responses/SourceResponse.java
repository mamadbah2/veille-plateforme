package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.MethodeCollecte;
import sn.ssi.veille.models.entities.Source.SourceType;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une source.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Informations d'une source de veille")
public record SourceResponse(
        // Identité
        @Schema(description = "Identifiant unique") String id,

        @Schema(description = "URL de la source") String url,

        @Schema(description = "Nom de la source") String nomSource,

        @Schema(description = "Description") String description,

        @Schema(description = "URL du logo") String logoUrl,

        // Collecte
        @Schema(description = "Méthode de collecte") MethodeCollecte methodeCollecte,

        @Schema(description = "Source active") boolean active,

        @Schema(description = "Fréquence de scraping en minutes") int frequenceScraping,

        @Schema(description = "Dernière synchronisation") LocalDateTime derniereSyncro,

        @Schema(description = "Prochaine synchronisation") LocalDateTime nextSyncAt,

        // Configuration
        @Schema(description = "Langue") String langue,

        @Schema(description = "Priorité (1-10)") int priorite,

        @Schema(description = "Catégorie par défaut") String categorieParDefaut,

        // Crédibilité
        @Schema(description = "Score de confiance (1-10)") int trustScore,

        @Schema(description = "Source vérifiée") boolean verified,

        @Schema(description = "Type de source") SourceType sourceType,

        // Statistiques
        @Schema(description = "Total articles collectés") long totalArticlesCollected,

        @Schema(description = "Articles dernière synchro") int articlesLastSync,

        // Erreurs
        @Schema(description = "Dernière erreur") String lastError,

        @Schema(description = "Date dernière erreur") LocalDateTime lastErrorAt,

        @Schema(description = "Échecs consécutifs") int consecutiveFailures,

        // Timestamps
        @Schema(description = "Date de création") LocalDateTime createdAt) {
}
