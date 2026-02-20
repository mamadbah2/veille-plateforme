package sn.ssi.veille.web.dto.responses;

/**
 * DTO de réponse pour l'authentification.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
 
public record AuthResponse(
        String accessToken,
        // String refreshToken,
        String tokenType,
        long expiresIn,
        UserResponse user
) {}
