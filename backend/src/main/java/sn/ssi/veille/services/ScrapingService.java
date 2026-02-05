package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Source;

import java.util.List;

/**
 * Interface du service de scraping/collecte de données.
 * 
 * <p>Ce service gère la collecte automatique d'articles depuis les sources configurées.</p>
 * 
 * <h3>Responsabilités :</h3>
 * <ul>
 *   <li>Scraping des sources selon leur méthode de collecte</li>
 *   <li>Parsing et normalisation des données</li>
 *   <li>Détection et évitement des doublons</li>
 *   <li>Planification des collectes</li>
 * </ul>
 * 
 * <h3>Points d'attention pour l'implémentation :</h3>
 * <ul>
 *   <li>Respecter les rate limits des APIs</li>
 *   <li>Gérer les erreurs de connexion gracieusement</li>
 *   <li>Utiliser Playwright pour les sites JavaScript (bonus)</li>
 *   <li>Logger les erreurs de scraping</li>
 * </ul>
 * 
 * <h3>Méthodes de collecte supportées :</h3>
 * <ul>
 *   <li><b>API</b> : Appels REST (Reddit API, etc.)</li>
 *   <li><b>RSS</b> : Parsing de flux RSS/Atom</li>
 *   <li><b>SCRAPING</b> : Parsing HTML (Jsoup)</li>
 *   <li><b>PLAYWRIGHT</b> : Sites avec rendu JavaScript (bonus)</li>
 * </ul>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
public interface ScrapingService {

    /**
     * Lance le scraping de toutes les sources actives.
     * 
     * <p>Peut être déclenché par un job planifié (Scheduler).</p>
     *
     * @return le nombre d'articles collectés
     */
    int scrapeAllSources();

    /**
     * Lance le scraping d'une source spécifique.
     *
     * @param sourceId l'ID de la source
     * @return la liste des articles collectés
     * @throws sn.ssi.veille.exceptions.SourceNotFoundException si la source n'existe pas
     * @throws sn.ssi.veille.exceptions.ScrapingException en cas d'erreur de scraping
     */
    List<Article> scrapeSource(String sourceId);

    /**
     * Collecte via API REST.
     * 
     * <p>Implémentation spécifique pour les APIs (Reddit, etc.).</p>
     *
     * @param source la source à scraper
     * @return la liste des articles collectés
     */
    List<Article> scrapeViaApi(Source source);

    /**
     * Collecte via flux RSS.
     * 
     * <p>Parse les flux RSS/Atom.</p>
     *
     * @param source la source à scraper
     * @return la liste des articles collectés
     */
    List<Article> scrapeViaRss(Source source);

    /**
     * Collecte via scraping HTML.
     * 
     * <p>Utilise Jsoup pour parser le HTML.</p>
     *
     * @param source la source à scraper
     * @return la liste des articles collectés
     */
    List<Article> scrapeViaHtml(Source source);

    /**
     * Collecte via Playwright (JavaScript rendering).
     * 
     * <p>Bonus : pour les sites avec rendu JavaScript.</p>
     *
     * @param source la source à scraper
     * @return la liste des articles collectés
     */
    List<Article> scrapeViaPlaywright(Source source);

    /**
     * Détermine la catégorie d'un article basé sur son contenu.
     * 
     * <p>Utilise des mots-clés ou un modèle ML simple.</p>
     *
     * @param article l'article à catégoriser
     * @return l'ID de la catégorie
     */
    String categorizeArticle(Article article);

    /**
     * Détermine le niveau de gravité d'un article.
     * 
     * <p>Analyse le contenu pour détecter les CVE, mots-clés critiques, etc.</p>
     *
     * @param article l'article à analyser
     * @return le niveau de gravité
     */
    sn.ssi.veille.models.entities.Gravite determineGravity(Article article);

    /**
     * Vérifie l'état de santé des scrapers.
     * 
     * <p>Utilisé pour le monitoring (bonus).</p>
     *
     * @return un rapport sur l'état des scrapers
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
