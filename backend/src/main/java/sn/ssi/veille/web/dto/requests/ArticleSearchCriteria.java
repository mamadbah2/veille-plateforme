package sn.ssi.veille.web.dto.requests;

import sn.ssi.veille.models.entities.Gravite;

import java.time.LocalDateTime;

/**
 * DTO pour les critères de recherche d'articles.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */

public record ArticleSearchCriteria(
                String query,
                String categorieId,
                String sourceId,
                Gravite graviteMin,
                LocalDateTime dateDebut,
                LocalDateTime dateFin,
                String[] tags,
                String auteur,
                Integer page,
                Integer size,
                String sortBy,
                String sortDirection) {
}
