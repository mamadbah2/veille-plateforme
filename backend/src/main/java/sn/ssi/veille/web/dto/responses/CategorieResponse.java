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
        String id,
        String nomCategorie,
        String description,
        String couleur,
        String icone,
        LocalDateTime createdAt

) {}
