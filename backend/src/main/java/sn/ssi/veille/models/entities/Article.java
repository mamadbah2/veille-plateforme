package sn.ssi.veille.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "articles")
@CompoundIndex(name = "url_source_idx", def = "{'urlOrigine': 1, 'sourceId': 1}", unique = true)
public class Article {

    /**
     * Identifiant unique de l'article.
     */
    @Id
    private String id;

    /**
     * Date de publication originale de l'article.
     */
    @Indexed
    private LocalDateTime datePublication;

    /**
     * Titre de l'article.
     */
    @TextIndexed(weight = 3)
    private String titre;

    /**
     * Contenu complet de l'article.
     */
    @TextIndexed
    private String contenu;

    /**
     * Résumé de l'article (généré ou extrait).
     */
    private String resume;

    /**
     * URL originale de l'article.
     */
    @Indexed
    private String urlOrigine;

    /**
     * Image de couverture de l'article.
     */
    private String imageUrl;

    /**
     * Identifiant de la source.
     */
    @Indexed
    private String sourceId;

    /**
     * Identifiant de la catégorie.
     */
    @Indexed
    private String categorieId;

    /**
     * Niveau de gravité/importance (1-5).
     * 1 = Information, 5 = Critique
     */
    @Builder.Default
    private Gravite gravite = Gravite.INFORMATION;

    /**
     * Tags/mots-clés associés.
     */
    private String[] tags;

    /**
     * Auteur de l'article (si disponible).
     */
    private String auteur;

    /**
     * Nombre de vues.
     */
    @Builder.Default
    private long nombreVues = 0;

    /**
     * Date de création dans le système.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Liste des IDs d'articles liés (Cross-referencing).
     */
    @Builder.Default
    private java.util.List<String> relatedArticleIds = new java.util.ArrayList<>();

    /**
     * Vecteur d'embedding (représentation sémantique).
     */
    private java.util.List<Double> vector;

    /**
     * ID de l'histoire (Cluster) auquel l'article appartient.
     */
    @Indexed
    private String storyId;
}
