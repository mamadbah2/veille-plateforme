package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création/modification d'une catégorie.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour créer ou modifier une catégorie")
public record CategorieRequest(

        @Schema(description = "Nom de la catégorie", example = "Vulnérabilités")
        @NotBlank(message = "Le nom de la catégorie est obligatoire")
        @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
        String nomCategorie,

        @Schema(description = "Description de la catégorie", example = "Articles sur les dernières vulnérabilités découvertes")
        @Size(max = 500, message = "La description ne peut pas dépasser 500 caractères")
        String description,

        @Schema(description = "Couleur HEX pour l'affichage", example = "#FF5733")
        String couleur,

        @Schema(description = "Nom de l'icône", example = "shield-alert")
        String icone

) {}
