package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une catégorie.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Informations d'une catégorie")
public record CategorieResponse(

        @Schema(description = "Identifiant unique", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        String id,

        @Schema(description = "Nom de la catégorie", example = "Vulnérabilités")
        String nomCategorie,

        @Schema(description = "Description de la catégorie")
        String description,

        @Schema(description = "Couleur HEX", example = "#FF5733")
        String couleur,

        @Schema(description = "Nom de l'icône", example = "shield-alert")
        String icone,

        @Schema(description = "Date de création")
        LocalDateTime createdAt

) {}
