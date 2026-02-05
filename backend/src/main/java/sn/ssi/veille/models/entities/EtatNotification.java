package sn.ssi.veille.models.entities;

/**
 * Énumération des états possibles d'une notification.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public enum EtatNotification {
    /**
     * La notification n'a pas encore été lue.
     */
    NON_LU,

    /**
     * La notification a été lue par l'utilisateur.
     */
    LU
}
