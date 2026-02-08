package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO pour la mise à jour du profil utilisateur.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour la mise à jour du profil utilisateur")
public record UpdateUserRequest(
        String username,
        String oldPassword,
        String newPassword

) {}
