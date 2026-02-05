package sn.ssi.veille.web.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import sn.ssi.veille.models.entities.EtatNotification;
import sn.ssi.veille.models.entities.TypeNotification;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une notification.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Informations d'une notification")
public record NotificationResponse(

        @Schema(description = "Identifiant unique", example = "65f1a2b3c4d5e6f7g8h9i0j1")
        String id,

        @Schema(description = "Titre de la notification")
        String titre,

        @Schema(description = "Contenu de la notification")
        String contenu,

        @Schema(description = "État de la notification")
        EtatNotification etat,

        @Schema(description = "Type de notification")
        TypeNotification type,

        @Schema(description = "ID de l'article associé")
        String articleId,

        @Schema(description = "URL d'action")
        String actionUrl,

        @Schema(description = "Date de création")
        LocalDateTime date

) {}
