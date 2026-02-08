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
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp

) {}
