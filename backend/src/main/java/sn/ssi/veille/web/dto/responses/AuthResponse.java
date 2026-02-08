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
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserResponse user
) {}
