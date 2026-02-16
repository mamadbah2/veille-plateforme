package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO pour la création/modification d'une catégorie.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour créer ou modifier une catégorie")
public record CategorieRequest(
                String nomCategorie,
                String description,
                String couleur,
                String icone) {
}
