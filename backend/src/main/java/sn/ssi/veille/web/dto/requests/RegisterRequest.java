package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la requête d'inscription d'un nouvel utilisateur.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données requises pour l'inscription d'un utilisateur")
public record RegisterRequest(

        @Schema(description = "Adresse email de l'utilisateur", example = "etudiant@ssi.sn")
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email doit être valide")
        String email,

        @Schema(description = "Nom d'utilisateur unique", example = "etudiant_ssi")
        @NotBlank(message = "Le nom d'utilisateur est obligatoire")
        @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
        String username,

        @Schema(description = "Mot de passe (min 8 caractères)", example = "MotDePasse123!")
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, max = 100, message = "Le mot de passe doit contenir entre 8 et 100 caractères")
        String password

) {}
