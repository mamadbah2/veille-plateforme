package sn.ssi.veille.web.dto.requests;

import sn.ssi.veille.models.entities.Gravite;
import java.time.LocalDateTime;

/**
 * DTO pour la création/modification d'un article.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public record ArticleRequest(
        String titre,
        String contenu,
        String resume,
        String urlOrigine,
        String imageUrl,
        String sourceId,
        String categorieId,
        Gravite gravite,
        String[] tags,
        String auteur,
        LocalDateTime datePublication
) {}
