package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.EtatNotification;
import sn.ssi.veille.models.entities.TypeNotification;
import sn.ssi.veille.web.dto.responses.NotificationResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

public interface NotificationService {

    NotificationResponse createNotification(String userId, String titre, String contenu, 
                                            TypeNotification type, String articleId);

    void notifyAllUsers(String titre, String contenu, TypeNotification type, String articleId);

    PageResponse<NotificationResponse> getCurrentUserNotifications(int page, int size);

    PageResponse<NotificationResponse> getNotificationsByEtat(EtatNotification etat, int page, int size);

    long getUnreadCount();

    NotificationResponse markAsRead(String notificationId);

    void markAllAsRead();

    void deleteNotification(String notificationId);

    void deleteAllNotifications();

    void cleanupOldNotifications();
}
