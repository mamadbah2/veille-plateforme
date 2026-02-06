package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Source;

import java.util.List;

/**
 * Service responsable de la collecte (scraping) d'articles depuis différentes sources.
 * Il gère l'extraction, la normalisation et l'enrichissement des données.
 * <p>
 * Ce service offre plusieurs stratégies de collecte (API, RSS, HTML, etc.),
 * en commençant par l'intégration via API.
 */
public interface ScrapingService {

    /**
     * Déclenche le scraping de toutes les sources actives configurées.
     * Cette méthode est généralement appelée par une tâche planifiée.
     *
     * @return Le nombre total d'articles collectés avec succès.
     */
    int scrapeAllSources();

    /**
     * Déclenche le scraping pour une source spécifique donnée par son identifiant.
     *
     * @param sourceId L'identifiant unique de la source à scraper.
     * @return La liste des nouveaux articles collectés depuis cette source.
     */
    List<Article> scrapeSource(String sourceId);

    /**
     * Collecte des articles via une API externe (ex: REST, JSON).
     * C'est la méthode principale d'implémentation actuelle.
     *
     * @param source La configuration de la source contenant l'URL API et les clés nécessaires.
     * @return Une liste d'objets Article normalisés.
     */
    List<Article> scrapeViaApi(Source source);

    // List<Article> scrapeViaRss(Source source);

    // List<Article> scrapeViaHtml(Source source);

    // List<Article> scrapeViaPlaywright(Source source);

    /**
     * Analyse le contenu d'un article pour déterminer sa catégorie automatiquement.
     *
     * @param article L'article à catégoriser.
     * @return Le nom de la catégorie déterminée (ex: "Vulnérabilité", "Cybercriminalité").
     */
    String categorizeArticle(Article article);

    /**
     * Détermine le niveau de gravité d'un article en fonction de mots-clés ou métriques (CVSS).
     *
     * @param article L'article à évaluer.
     * @return L'énumération Gravite correspondant au niveau de risque.
     */
    sn.ssi.veille.models.entities.Gravite determineGravity(Article article);

    /**
     * Génère un rapport complet sur l'état du système de scraping.
     * Utile pour le monitoring et la détection d'erreurs.
     *
     * @return Un objet record contenant les statistiques et états par source.
     */
    ScrapingHealthReport getHealthReport();

    /**
     * Rapport sur l'état de santé des scrapers.
     */
    record ScrapingHealthReport(
        int activeSources,
        int totalArticlesCollected,
        int failedSources,
        java.time.LocalDateTime lastRun,
        java.util.List<SourceStatus> sourceStatuses
    ) {
        public record SourceStatus(
            String sourceId,
            String sourceName,
            boolean healthy,
            String lastError,
            java.time.LocalDateTime lastSuccessfulSync
        ) {}
    }
}
