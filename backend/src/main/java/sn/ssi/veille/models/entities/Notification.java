package sn.ssi.veille.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entité représentant une notification envoyée à un utilisateur.
 * Liée à un article ou un événement système.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
@CompoundIndex(name = "user_etat_idx", def = "{'userId': 1, 'etat': 1}")
public class Notification {

    /**
     * Identifiant unique de la notification.
     */
    @Id
    private String id;

    /**
     * Identifiant de l'utilisateur destinataire.
     */
    @Indexed
    private String userId;

    /**
     * Date de création de la notification.
     */
    @CreatedDate
    private LocalDateTime date;

    /**
     * Contenu/message de la notification.
     */
    private String contenu;

    /**
     * Titre de la notification.
     */
    private String titre;

    /**
     * État de la notification (lue/non lue).
     */
    @Builder.Default
    private EtatNotification etat = EtatNotification.NON_LU;

    /**
     * Type de notification.
     */
    @Builder.Default
    private TypeNotification type = TypeNotification.ARTICLE;

    /**
     * Identifiant de l'article associé (si applicable).
     */
    private String articleId;

    /**
     * URL d'action associée.
     */
    private String actionUrl;
}
