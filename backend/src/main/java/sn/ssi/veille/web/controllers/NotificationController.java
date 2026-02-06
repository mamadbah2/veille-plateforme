package sn.ssi.veille.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.models.entities.EtatNotification;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.NotificationResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

@RequestMapping("/api/v1/notifications")
public interface NotificationController {

    @GetMapping
    ResponseEntity<PageResponse<NotificationResponse>> getMyNotifications(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    @GetMapping("/etat/{etat}")
    ResponseEntity<PageResponse<NotificationResponse>> getNotificationsByEtat(
        @PathVariable EtatNotification etat,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    @GetMapping("/unread-count")
    ResponseEntity<Long> getUnreadCount();

    @PatchMapping("/{id}/read")
    ResponseEntity<NotificationResponse> markAsRead(
        @PathVariable String id
    );

    @PatchMapping("/read-all")
    ResponseEntity<MessageResponse> markAllAsRead();

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteNotification(
        @PathVariable String id
    );

    @DeleteMapping
    ResponseEntity<MessageResponse> deleteAllNotifications();
}
