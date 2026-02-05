package sn.ssi.veille.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.models.entities.EtatNotification;
import sn.ssi.veille.web.dto.responses.ErrorResponse;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.NotificationResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

/**
 * Interface du contrôleur de gestion des notifications.
 * 
 * <p>Gère les notifications de l'utilisateur connecté.</p>
 * 
 * <h3>Sécurité :</h3>
 * <ul>
 *   <li>Tous les endpoints nécessitent une authentification</li>
 *   <li>L'utilisateur ne peut voir que ses propres notifications</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Tag(name = "Notifications", description = "API de gestion des notifications utilisateur")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/notifications")
public interface NotificationController {

    /**
     * Récupère les notifications de l'utilisateur connecté.
     *
     * @param page numéro de page
     * @param size taille de la page
     * @return une page de notifications
     */
    @Operation(
        summary = "Mes notifications",
        description = "Récupère les notifications de l'utilisateur connecté avec pagination."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Non authentifié",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping
    ResponseEntity<PageResponse<NotificationResponse>> getMyNotifications(
        @Parameter(description = "Numéro de page", example = "0")
        @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Taille de la page", example = "20")
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère les notifications par état.
     *
     * @param etat l'état des notifications (LU/NON_LU)
     * @param page numéro de page
     * @param size taille de la page
     * @return une page de notifications
     */
    @Operation(
        summary = "Notifications par état",
        description = "Récupère les notifications filtrées par état (LU/NON_LU)."
    )
    @ApiResponse(responseCode = "200", description = "Liste récupérée")
    @GetMapping("/etat/{etat}")
    ResponseEntity<PageResponse<NotificationResponse>> getNotificationsByEtat(
        @Parameter(description = "État des notifications", required = true)
        @PathVariable EtatNotification etat,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    /**
     * Récupère le nombre de notifications non lues.
     *
     * @return le compteur de notifications non lues
     */
    @Operation(
        summary = "Compteur non lues",
        description = "Récupère le nombre de notifications non lues."
    )
    @ApiResponse(responseCode = "200", description = "Compteur récupéré")
    @GetMapping("/unread-count")
    ResponseEntity<Long> getUnreadCount();

    /**
     * Marque une notification comme lue.
     *
     * @param id l'ID de la notification
     * @return la notification mise à jour
     */
    @Operation(
        summary = "Marquer comme lue",
        description = "Marque une notification spécifique comme lue."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Notification marquée comme lue",
            content = @Content(schema = @Schema(implementation = NotificationResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notification non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PatchMapping("/{id}/read")
    ResponseEntity<NotificationResponse> markAsRead(
        @Parameter(description = "ID de la notification", required = true)
        @PathVariable String id
    );

    /**
     * Marque toutes les notifications comme lues.
     *
     * @return message de confirmation
     */
    @Operation(
        summary = "Tout marquer comme lu",
        description = "Marque toutes les notifications de l'utilisateur comme lues."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Notifications marquées comme lues",
        content = @Content(schema = @Schema(implementation = MessageResponse.class))
    )
    @PatchMapping("/read-all")
    ResponseEntity<MessageResponse> markAllAsRead();

    /**
     * Supprime une notification.
     *
     * @param id l'ID de la notification
     * @return aucun contenu
     */
    @Operation(
        summary = "Supprimer une notification",
        description = "Supprime une notification spécifique."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Notification supprimée"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notification non trouvée",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteNotification(
        @Parameter(description = "ID de la notification", required = true)
        @PathVariable String id
    );

    /**
     * Supprime toutes les notifications.
     *
     * @return message de confirmation
     */
    @Operation(
        summary = "Supprimer toutes les notifications",
        description = "Supprime toutes les notifications de l'utilisateur."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Notifications supprimées",
        content = @Content(schema = @Schema(implementation = MessageResponse.class))
    )
    @DeleteMapping
    ResponseEntity<MessageResponse> deleteAllNotifications();
}
