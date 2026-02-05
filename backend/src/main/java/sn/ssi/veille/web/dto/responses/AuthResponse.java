package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de réponse pour l'authentification.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Réponse d'authentification avec tokens JWT")
public record AuthResponse(

        @Schema(description = "Token d'accès JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "Token de rafraîchissement", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken,

        @Schema(description = "Type de token", example = "Bearer")
        String tokenType,

        @Schema(description = "Durée de validité en secondes", example = "3600")
        long expiresIn,

        @Schema(description = "Informations de l'utilisateur authentifié")
        UserResponse user

) {}
