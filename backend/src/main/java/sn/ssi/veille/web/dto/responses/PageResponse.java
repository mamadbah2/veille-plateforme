package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO de réponse paginée générique.
 *
 * @param <T> le type d'éléments dans la page
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Réponse paginée")
public record PageResponse<T>(

        @Schema(description = "Liste des éléments de la page")
        List<T> content,

        @Schema(description = "Numéro de la page (0-indexed)", example = "0")
        int page,

        @Schema(description = "Taille de la page", example = "20")
        int size,

        @Schema(description = "Nombre total d'éléments", example = "150")
        long totalElements,

        @Schema(description = "Nombre total de pages", example = "8")
        int totalPages,

        @Schema(description = "Première page", example = "true")
        boolean first,

        @Schema(description = "Dernière page", example = "false")
        boolean last

) {}
