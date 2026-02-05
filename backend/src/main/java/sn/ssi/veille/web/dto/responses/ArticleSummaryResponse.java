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

        @Schema(description = "Identifiant unique", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        String id,

        @Schema(description = "Titre de l'article")
        String titre,

        @Schema(description = "Résumé ou extrait de l'article")
        String resume,

        @Schema(description = "URL de l'image de couverture")
        String imageUrl,

        @Schema(description = "Niveau de gravité")
        Gravite gravite,

        @Schema(description = "Nom de la source")
        String nomSource,

        @Schema(description = "Nom de la catégorie")
        String nomCategorie,

        @Schema(description = "Couleur de la catégorie")
        String couleurCategorie,

        @Schema(description = "Nombre de vues")
        long nombreVues,

        @Schema(description = "Date de publication")
        LocalDateTime datePublication

) {}
