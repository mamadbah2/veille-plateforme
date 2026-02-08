package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.Gravite;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un article en format résumé (liste).
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Résumé d'un article pour affichage en liste")
public record ArticleSummaryResponse(
        String id,
        String titre,
        String resume,
        String imageUrl,
        Gravite gravite,
        String nomSource,
        String nomCategorie,
        String couleurCategorie,
        long nombreVues,
        LocalDateTime datePublication
) {}
