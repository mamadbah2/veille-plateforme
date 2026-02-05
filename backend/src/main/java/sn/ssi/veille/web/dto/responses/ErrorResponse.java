package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO de réponse d'erreur standard.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Réponse d'erreur")
public record ErrorResponse(

        @Schema(description = "Code d'erreur HTTP", example = "400")
        int status,

        @Schema(description = "Type d'erreur", example = "Bad Request")
        String error,

        @Schema(description = "Message d'erreur détaillé", example = "L'email est déjà utilisé")
        String message,

        @Schema(description = "Chemin de la requête", example = "/api/v1/auth/register")
        String path,

        @Schema(description = "Horodatage de l'erreur")
        LocalDateTime timestamp

) {}
