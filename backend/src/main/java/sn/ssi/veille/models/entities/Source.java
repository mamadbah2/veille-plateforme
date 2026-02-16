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
import java.util.Map;

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

    /** Type de source pour la classification */
    public enum SourceType {
        OFFICIAL, // Source officielle (NIST, CISA, CERT-FR)
        MEDIA, // Média reconnu (TechCrunch, Wired)
        BLOG, // Blog expert (Krebs, Schneier)
        COMMUNITY, // Communauté (Reddit, Hacker News)
        UNKNOWN // Non vérifié
    }

    // ==================== IDENTITÉ ====================

    /** Identifiant unique MongoDB */
    @Id
    private String id;

    /** URL de base de la source (unique) */
    @Indexed(unique = true)
    private String url;

    /** Nom de la source (ex: "Reddit NetSec", "NIST NVD") */
    private String nomSource;

    /** Description de la source pour l'affichage */
    private String description;

    /** URL du logo pour l'interface */
    private String logoUrl;

    // ==================== COLLECTE ====================

    /** Méthode de collecte : API, SCRAPING, RSS */
    private MethodeCollecte methodeCollecte;

    /** Indique si la source est active pour le scraping */
    @Builder.Default
    private boolean active = true;

    /** Fréquence de scraping en minutes */
    @Builder.Default
    private int frequenceScraping = 60;

    /** Date de dernière collecte réussie */
    private LocalDateTime derniereSyncro;

    /** Date de la prochaine synchronisation prévue */
    private LocalDateTime nextSyncAt;

    // ==================== AUTHENTIFICATION ====================

    /** Clé API si la source nécessite une authentification */
    private String apiKey;

    /** En-têtes HTTP personnalisés (ex: Authorization, User-Agent) */
    private Map<String, String> headers;

    // ==================== CONFIGURATION ====================

    /** Langue du contenu : FR, EN */
    private String langue;

    /** Priorité de scraping (1-10, 10 = plus haute) */
    @Builder.Default
    private int priorite = 5;

    /** ID de la catégorie par défaut pour les articles */
    private String categorieParDefaut;

    /** Timeout en secondes pour les requêtes HTTP */
    @Builder.Default
    private int timeout = 30;

    /** Nombre de tentatives en cas d'échec */
    @Builder.Default
    private int retryCount = 3;

    /** Limite de requêtes par minute (rate limiting) */
    @Builder.Default
    private int rateLimitPerMinute = 60;

    /** Nombre maximum d'articles à collecter par synchronisation */
    @Builder.Default
    private int maxArticlesPerSync = 100;

    // ==================== CRÉDIBILITÉ ====================

    /** Score de confiance (1-10) : 10 = source officielle */
    @Builder.Default
    private int trustScore = 5;

    /** Indique si la source a été vérifiée par un admin */
    @Builder.Default
    private boolean verified = false;

    /** Type de source : OFFICIAL, MEDIA, BLOG, COMMUNITY, UNKNOWN */
    @Builder.Default
    private SourceType sourceType = SourceType.UNKNOWN;

    // ==================== GESTION DES ERREURS ====================

    /** Message de la dernière erreur rencontrée */
    private String lastError;

    /** Date de la dernière erreur */
    private LocalDateTime lastErrorAt;

    /** Nombre d'échecs consécutifs (reset après succès) */
    @Builder.Default
    private int consecutiveFailures = 0;

    // ==================== STATISTIQUES ====================

    /** Nombre total d'articles collectés depuis cette source */
    @Builder.Default
    private long totalArticlesCollected = 0;

    /** Nombre d'articles collectés lors de la dernière synchro */
    @Builder.Default
    private int articlesLastSync = 0;

    // ==================== SCRAPING HTML ====================

    /** Sélecteur CSS pour extraire le titre (ex: "h1.article-title") */
    private String selectorTitle;

    /** Sélecteur CSS pour extraire le contenu */
    private String selectorContent;

    /** Sélecteur CSS pour extraire la date */
    private String selectorDate;

    // ==================== TIMESTAMPS ====================

    /** Date de création */
    @CreatedDate
    private LocalDateTime createdAt;

    /** Date de dernière modification */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
