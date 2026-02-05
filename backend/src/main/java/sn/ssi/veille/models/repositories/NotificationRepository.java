package sn.ssi.veille.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sn.ssi.veille.models.entities.EtatNotification;
import sn.ssi.veille.models.entities.Notification;
import sn.ssi.veille.models.entities.TypeNotification;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository MongoDB pour l'entité Notification.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Récupère les notifications d'un utilisateur avec pagination.
     *
     * @param userId l'ID de l'utilisateur
     * @param pageable les paramètres de pagination
     * @return une page de notifications
     */
    Page<Notification> findByUserId(String userId, Pageable pageable);

    /**
     * Récupère les notifications d'un utilisateur triées par date.
     *
     * @param userId l'ID de l'utilisateur
     * @param pageable les paramètres de pagination
     * @return une page de notifications
     */
    Page<Notification> findByUserIdOrderByDateDesc(String userId, Pageable pageable);

    /**
     * Récupère les notifications non lues d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @return la liste des notifications non lues
     */
    List<Notification> findByUserIdAndEtat(String userId, EtatNotification etat);

    /**
     * Récupère les notifications d'un utilisateur par type.
     *
     * @param userId l'ID de l'utilisateur
     * @param type le type de notification
     * @param pageable les paramètres de pagination
     * @return une page de notifications
     */
    Page<Notification> findByUserIdAndType(String userId, TypeNotification type, Pageable pageable);

    /**
     * Compte les notifications non lues d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @param etat l'état des notifications
     * @return le nombre de notifications
     */
    long countByUserIdAndEtat(String userId, EtatNotification etat);

    /**
     * Supprime les notifications antérieures à une date.
     *
     * @param date la date limite
     */
    void deleteByDateBefore(LocalDateTime date);

    /**
     * Supprime toutes les notifications d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     */
    void deleteByUserId(String userId);
}
