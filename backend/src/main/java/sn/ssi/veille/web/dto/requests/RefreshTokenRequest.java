package sn.ssi.veille.web.dto.requests;

/**
 * DTO pour le rafraîchissement du token JWT.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */

public record RefreshTokenRequest(
        String refreshToken

) {}
