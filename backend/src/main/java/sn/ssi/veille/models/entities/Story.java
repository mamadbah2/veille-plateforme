package sn.ssi.veille.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Une Story représente un sujet d'actualité regroupant plusieurs articles.
 * Elle dispose de son propre titre et résumé synthétisés par l'IA.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stories")
public class Story {

    @org.springframework.data.annotation.Id
    private String id;

    @org.springframework.data.annotation.CreatedDate
    private LocalDateTime dateCreation;

    @org.springframework.data.annotation.LastModifiedDate
    private LocalDateTime dateModification;

    private String titre;

    private String resume;

    private LocalDateTime dateMiseAJour;

    private EtatStory etat;

    // Union des catégories des articles sources
    @Builder.Default
    private Set<String> categories = new HashSet<>();

    // Liste des articles liés à cette story
    @DBRef
    private List<Article> articles;

    public enum EtatStory {
        DRAFT,
        PUBLISHED,
        ARCHIVED
    }
}
