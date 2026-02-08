package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la mise à jour d'un favori.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour mettre à jour un favori")
public record UpdateFavorisRequest(
        String note,
        String[] tagsPersonnels

) {}
