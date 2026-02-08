package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour la requête de connexion.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données requises pour la connexion")
public record LoginRequest(
        String identifier,
        String password

) {}
