package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.Role;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO de réponse pour les informations utilisateur.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Informations de l'utilisateur")
public record UserResponse(

        @Schema(description = "Identifiant unique", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        String id,

        @Schema(description = "Adresse email", example = "etudiant@ssi.sn")
        String email,

        @Schema(description = "Nom d'utilisateur", example = "etudiant_ssi")
        String username,

        @Schema(description = "Rôles de l'utilisateur")
        Set<Role> roles,

        @Schema(description = "Compte actif", example = "true")
        boolean enabled,

        @Schema(description = "Date de création du compte")
        LocalDateTime createdAt

) {}
