package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.MethodeCollecte;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une source.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Informations d'une source de veille")
public record SourceResponse(

        @Schema(description = "Identifiant unique", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        String id,

        @Schema(description = "URL de la source", example = "https://www.reddit.com/r/netsec")
        String url,

        @Schema(description = "Nom de la source", example = "Reddit NetSec")
        String nomSource,

        @Schema(description = "Méthode de collecte", example = "API")
        MethodeCollecte methodeCollecte,

        @Schema(description = "Source active", example = "true")
        boolean active,

        @Schema(description = "Fréquence de scraping en minutes", example = "60")
        int frequenceScraping,

        @Schema(description = "Date de dernière synchronisation")
        LocalDateTime derniereSyncro,

        @Schema(description = "Date de création")
        LocalDateTime createdAt

) {}
