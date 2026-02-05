package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de réponse générique pour les opérations simples.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Réponse de succès générique")
public record MessageResponse(

        @Schema(description = "Message de succès", example = "Opération effectuée avec succès")
        String message,

        @Schema(description = "Succès de l'opération", example = "true")
        boolean success

) {
    /**
     * Crée une réponse de succès avec un message.
     *
     * @param message le message de succès
     * @return une réponse de succès
     */
    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    /**
     * Crée une réponse d'échec avec un message.
     *
     * @param message le message d'erreur
     * @return une réponse d'échec
     */
    public static MessageResponse failure(String message) {
        return new MessageResponse(message, false);
    }
}
