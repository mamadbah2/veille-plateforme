package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.Gravite;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un article.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Informations complètes d'un article")
public record ArticleResponse(

        @Schema(description = "Identifiant unique", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        String id,

        @Schema(description = "Titre de l'article")
        String titre,

        @Schema(description = "Contenu complet de l'article")
        String contenu,

        @Schema(description = "Résumé de l'article")
        String resume,

        @Schema(description = "URL originale de l'article")
        String urlOrigine,

        @Schema(description = "URL de l'image de couverture")
        String imageUrl,

        @Schema(description = "Niveau de gravité")
        Gravite gravite,

        @Schema(description = "Tags associés")
        String[] tags,

        @Schema(description = "Auteur de l'article")
        String auteur,

        @Schema(description = "Nombre de vues")
        long nombreVues,

        @Schema(description = "Date de publication originale")
        LocalDateTime datePublication,

        @Schema(description = "Informations de la source")
        SourceResponse source,

        @Schema(description = "Informations de la catégorie")
        CategorieResponse categorie,

        @Schema(description = "Date de création dans le système")
        LocalDateTime createdAt

) {}
