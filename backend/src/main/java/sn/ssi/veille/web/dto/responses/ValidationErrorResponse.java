package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO de réponse pour les erreurs de validation.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Réponse d'erreur de validation")
public record ValidationErrorResponse(

        @Schema(description = "Code d'erreur HTTP", example = "400")
        int status,

        @Schema(description = "Type d'erreur", example = "Validation Error")
        String error,

        @Schema(description = "Message d'erreur général", example = "Erreurs de validation")
        String message,

        @Schema(description = "Détails des erreurs par champ")
        Map<String, List<String>> fieldErrors,

        @Schema(description = "Chemin de la requête", example = "/api/v1/auth/register")
        String path,

        @Schema(description = "Horodatage de l'erreur")
        LocalDateTime timestamp

) {}
