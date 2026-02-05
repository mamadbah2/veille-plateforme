package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO pour la requête de connexion.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données requises pour la connexion")
public record LoginRequest(

        @Schema(description = "Email ou nom d'utilisateur", example = "etudiant@ssi.sn")
        @NotBlank(message = "L'identifiant est obligatoire")
        String identifier,

        @Schema(description = "Mot de passe", example = "MotDePasse123!")
        @NotBlank(message = "Le mot de passe est obligatoire")
        String password

) {}
