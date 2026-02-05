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
 * Entité représentant un article mis en favori par un utilisateur.
 * Permet aux utilisateurs de sauvegarder des articles pour lecture ultérieure.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "favoris")
@CompoundIndex(name = "user_article_idx", def = "{'userId': 1, 'articleId': 1}", unique = true)
public class Favoris {

    /**
     * Identifiant unique du favori.
     */
    @Id
    private String id;

    /**
     * Identifiant de l'utilisateur.
     */
    @Indexed
    private String userId;

    /**
     * Identifiant de l'article.
     */
    @Indexed
    private String articleId;

    /**
     * Note personnelle de l'utilisateur sur l'article.
     */
    private String note;

    /**
     * Tags personnels ajoutés par l'utilisateur.
     */
    private String[] tagsPersonnels;

    /**
     * Date d'ajout aux favoris.
     */
    @CreatedDate
    private LocalDateTime createdAt;
}
