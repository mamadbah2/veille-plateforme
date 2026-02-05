package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.EtatNotification;
import sn.ssi.veille.models.entities.TypeNotification;
import sn.ssi.veille.web.dto.responses.NotificationResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

/**
 * Interface du service de gestion des notifications.
 * 
 * <p>Ce service gère l'envoi et la gestion des notifications utilisateur.</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Création et envoi de notifications</li>
 *   <li>Marquage lu/non lu</li>
 *   <li>Suppression et nettoyage</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Envoyer des notifications lors de l'ajout d'articles critiques</li>
 *   <li>Permettre le filtrage par type et état</li>
 *   <li>Prévoir un mécanisme de nettoyage des anciennes notifications</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public interface NotificationService {

    /**
     * Crée une notification pour un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @param titre le titre de la notification
     * @param contenu le contenu de la notification
     * @param type le type de notification
     * @param articleId l'ID de l'article associé (optionnel)
     * @return la notification créée
     */
    NotificationResponse createNotification(String userId, String titre, String contenu, 
                                            TypeNotification type, String articleId);

    /**
     * Envoie une notification à tous les utilisateurs.
     * 
     * <p>Utilisé pour les alertes système ou les articles critiques.</p>
     *
     * @param titre le titre de la notification
     * @param contenu le contenu de la notification
     * @param type le type de notification
     * @param articleId l'ID de l'article associé (optionnel)
     */
    void notifyAllUsers(String titre, String contenu, TypeNotification type, String articleId);

    /**
     * Récupère les notifications de l'utilisateur connecté.
     *
     * @param page numéro de la page
     * @param size taille de la page
     * @return une page de notifications
     */
    PageResponse<NotificationResponse> getCurrentUserNotifications(int page, int size);

    /**
     * Récupère les notifications par état.
     *
     * @param etat l'état des notifications (LU/NON_LU)
     * @param page numéro de la page
     * @param size taille de la page
     * @return une page de notifications
     */
    PageResponse<NotificationResponse> getNotificationsByEtat(EtatNotification etat, int page, int size);

    /**
     * Récupère le nombre de notifications non lues.
     *
     * @return le nombre de notifications non lues
     */
    long getUnreadCount();

    /**
     * Marque une notification comme lue.
     *
     * @param notificationId l'ID de la notification
     * @return la notification mise à jour
     * @throws sn.ssi.veille.exceptions.NotificationNotFoundException si la notification n'existe pas
     */
    NotificationResponse markAsRead(String notificationId);

    /**
     * Marque toutes les notifications comme lues.
     */
    void markAllAsRead();

    /**
     * Supprime une notification.
     *
     * @param notificationId l'ID de la notification
     * @throws sn.ssi.veille.exceptions.NotificationNotFoundException si la notification n'existe pas
     */
    void deleteNotification(String notificationId);

    /**
     * Supprime toutes les notifications de l'utilisateur connecté.
     */
    void deleteAllNotifications();

    /**
     * Nettoie les anciennes notifications (plus de 30 jours).
     * 
     * <p>Peut être appelé par un job planifié.</p>
     */
    void cleanupOldNotifications();
}
