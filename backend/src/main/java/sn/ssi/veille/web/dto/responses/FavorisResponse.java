package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un favori.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Informations d'un article favori")
public record FavorisResponse(

        @Schema(description = "Identifiant unique du favori", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        String id,

        @Schema(description = "Informations de l'article")
        ArticleSummaryResponse article,

        @Schema(description = "Note personnelle de l'utilisateur")
        String note,

        @Schema(description = "Tags personnels")
        String[] tagsPersonnels,

        @Schema(description = "Date d'ajout aux favoris")
        LocalDateTime createdAt

) {}
