package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sn.ssi.veille.models.entities.MethodeCollecte;

/**
 * DTO pour la création/modification d'une source.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour créer ou modifier une source de veille")
public record SourceRequest(

        @Schema(description = "URL de la source", example = "https://www.reddit.com/r/netsec")
        @NotBlank(message = "L'URL est obligatoire")
        String url,

        @Schema(description = "Nom de la source", example = "Reddit NetSec")
        @NotBlank(message = "Le nom de la source est obligatoire")
        String nomSource,

        @Schema(description = "Méthode de collecte", example = "API")
        @NotNull(message = "La méthode de collecte est obligatoire")
        MethodeCollecte methodeCollecte,

        @Schema(description = "Source active pour le scraping", example = "true")
        Boolean active,

        @Schema(description = "Fréquence de scraping en minutes", example = "60")
        @Positive(message = "La fréquence doit être positive")
        Integer frequenceScraping

) {}
