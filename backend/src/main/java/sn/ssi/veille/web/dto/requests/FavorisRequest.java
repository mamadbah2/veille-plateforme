package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour l'ajout d'un article aux favoris.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour ajouter un article aux favoris")
public record FavorisRequest(

        @Schema(description = "ID de l'article à ajouter aux favoris", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        @NotBlank(message = "L'ID de l'article est obligatoire")
        String articleId,

        @Schema(description = "Note personnelle sur l'article", example = "À relire pour le projet de sécurité")
        @Size(max = 500, message = "La note ne peut pas dépasser 500 caractères")
        String note,

        @Schema(description = "Tags personnels", example = "[\"important\", \"projet\"]")
        String[] tagsPersonnels

) {}
