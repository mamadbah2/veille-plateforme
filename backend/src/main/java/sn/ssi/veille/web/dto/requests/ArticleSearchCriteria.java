package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.Gravite;

import java.time.LocalDateTime;

/**
 * DTO pour les critères de recherche d'articles.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Critères de recherche et filtrage des articles")
public record ArticleSearchCriteria(

        @Schema(description = "Recherche textuelle (titre, contenu)", example = "log4j vulnerability")
        String query,

        @Schema(description = "ID de la catégorie pour filtrer")
        String categorieId,

        @Schema(description = "ID de la source pour filtrer")
        String sourceId,

        @Schema(description = "Niveau de gravité minimum", example = "IMPORTANT")
        Gravite graviteMin,

        @Schema(description = "Date de début pour la recherche")
        LocalDateTime dateDebut,

        @Schema(description = "Date de fin pour la recherche")
        LocalDateTime dateFin,

        @Schema(description = "Tags à rechercher", example = "[\"security\", \"CVE\"]")
        String[] tags,

        @Schema(description = "Auteur à rechercher")
        String auteur,

        @Schema(description = "Numéro de page (0-indexed)", example = "0")
        Integer page,

        @Schema(description = "Taille de la page", example = "20")
        Integer size,

        @Schema(description = "Champ de tri", example = "datePublication")
        String sortBy,

        @Schema(description = "Direction du tri (ASC/DESC)", example = "DESC")
        String sortDirection

) {}
