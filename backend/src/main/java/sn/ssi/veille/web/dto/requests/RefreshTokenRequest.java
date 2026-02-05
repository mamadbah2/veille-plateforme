package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour le rafraîchissement du token JWT.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour rafraîchir le token d'authentification")
public record RefreshTokenRequest(

        @Schema(description = "Token de rafraîchissement", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        @NotBlank(message = "Le refresh token est obligatoire")
        String refreshToken

) {}
