package sn.ssi.veille.web.dto.requests;

/**
 * DTO pour la requête d'inscription d'un nouvel utilisateur.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public record RegisterRequest(
        String email,
        String username,
        String password

) {}
