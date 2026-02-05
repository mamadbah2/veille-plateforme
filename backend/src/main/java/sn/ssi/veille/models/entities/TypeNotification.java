package sn.ssi.veille.models.entities;

/**
 * Énumération des types de notifications.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public enum TypeNotification {
    /**
     * Notification liée à un nouvel article.
     */
    ARTICLE,

    /**
     * Notification d'alerte de sécurité.
     */
    ALERTE,

    /**
     * Notification système (maintenance, etc.).
     */
    SYSTEME,

    /**
     * Notification de résumé hebdomadaire.
     */
    RESUME_HEBDO
}
