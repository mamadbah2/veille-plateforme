package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la mise à jour du profil utilisateur.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour la mise à jour du profil utilisateur")
public record UpdateUserRequest(

        @Schema(description = "Nouveau nom d'utilisateur", example = "nouveau_pseudo")
        @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
        String username,

        @Schema(description = "Ancien mot de passe (requis pour changer le mot de passe)")
        String oldPassword,

        @Schema(description = "Nouveau mot de passe", example = "NouveauMotDePasse123!")
        @Size(min = 8, max = 100, message = "Le mot de passe doit contenir entre 8 et 100 caractères")
        String newPassword

) {}
