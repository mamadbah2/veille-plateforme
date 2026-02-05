package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de réponse pour le résumé hebdomadaire.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Résumé hebdomadaire de veille")
public record WeeklySummaryResponse(

        @Schema(description = "Date de début de la semaine")
        LocalDateTime dateDebut,

        @Schema(description = "Date de fin de la semaine")
        LocalDateTime dateFin,

        @Schema(description = "Nombre total d'articles collectés", example = "45")
        long totalArticles,

        @Schema(description = "Articles les plus importants de la semaine")
        List<ArticleSummaryResponse> topArticles,

        @Schema(description = "Résumé textuel généré par IA")
        String resumeIA,

        @Schema(description = "Statistiques par catégorie")
        List<CategoryStats> statsByCategory,

        @Schema(description = "Statistiques par niveau de gravité")
        List<GraviteStats> statsByGravite

) {
    /**
     * Statistiques par catégorie.
     */
    @Schema(description = "Statistiques d'une catégorie")
    public record CategoryStats(
            @Schema(description = "Nom de la catégorie")
            String nomCategorie,
            
            @Schema(description = "Nombre d'articles")
            long count,
            
            @Schema(description = "Couleur de la catégorie")
            String couleur
    ) {}

    /**
     * Statistiques par niveau de gravité.
     */
    @Schema(description = "Statistiques par gravité")
    public record GraviteStats(
            @Schema(description = "Niveau de gravité")
            String gravite,
            
            @Schema(description = "Nombre d'articles")
            long count
    ) {}
}
