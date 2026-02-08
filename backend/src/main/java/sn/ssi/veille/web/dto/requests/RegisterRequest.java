package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO pour la requête d'inscription d'un nouvel utilisateur.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données requises pour l'inscription d'un utilisateur")
public record RegisterRequest(
        String email,
        String username,
        String password

) {}
