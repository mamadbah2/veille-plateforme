package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.MethodeCollecte;

/**
 * DTO pour la création/modification d'une source.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour créer ou modifier une source de veille")
public record SourceRequest(
        String url,
        String nomSource,
        MethodeCollecte methodeCollecte,
        Boolean active,
        Integer frequenceScraping

) {}
