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
        String id,
        String titre,
        String contenu,
        String resume,
        String urlOrigine,
        String imageUrl,
        Gravite gravite,
        String[] tags,
        String auteur,
        long nombreVues,
        LocalDateTime datePublication,
        SourceResponse source,
        CategorieResponse categorie,
        LocalDateTime createdAt

) {}
