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
        String articleId,
        String note,
        String[] tagsPersonnels

) {}
