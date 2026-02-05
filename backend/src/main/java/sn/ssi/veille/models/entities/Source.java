package sn.ssi.veille.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entité représentant une source de données pour la veille.
 * Exemples : Reddit, YCombinator, NIST, etc.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sources")
public class Source {

    /**
     * Identifiant unique de la source.
     */
    @Id
    private String id;

    /**
     * URL de base de la source.
     */
    @Indexed(unique = true)
    private String url;

    /**
     * Nom de la source (ex: Reddit, NIST).
     */
    private String nomSource;

    /**
     * Méthode de collecte utilisée (API, SCRAPING, RSS).
     */
    private MethodeCollecte methodeCollecte;

    /**
     * Indique si la source est active pour le scraping.
     */
    @Builder.Default
    private boolean active = true;

    /**
     * Fréquence de scraping en minutes.
     */
    @Builder.Default
    private int frequenceScraping = 60;

    /**
     * Date de dernière collecte réussie.
     */
    private LocalDateTime derniereSyncro;

    /**
     * Date de création.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
