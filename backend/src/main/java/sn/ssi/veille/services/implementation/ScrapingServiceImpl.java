package sn.ssi.veille.services.implementation;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import sn.ssi.veille.models.entities.*;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.services.AIService;
import sn.ssi.veille.services.CrossReferenceService;
import sn.ssi.veille.services.ScrapingService;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de scraping.
 * Supporte RSS, API REST et HTML scraping.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Service
@Slf4j
public class ScrapingServiceImpl implements ScrapingService {

    private final sn.ssi.veille.models.repositories.SourceRepository sourceRepository;
    private final sn.ssi.veille.models.repositories.ArticleRepository articleRepository;
    private final WebClient webClient;
    private final AIService aiService;
    private final CrossReferenceService crossReferenceService;
    private final sn.ssi.veille.models.repositories.CategorieRepository categorieRepository;
    private final sn.ssi.veille.services.ClusteringService clusteringService;
    private final sn.ssi.veille.services.ContentExtractionService contentExtractionService;

    public ScrapingServiceImpl(SourceRepository sourceRepository,
            ArticleRepository articleRepository,
            WebClient.Builder webClientBuilder,
            AIService aiService,
            CrossReferenceService crossReferenceService,
            sn.ssi.veille.models.repositories.CategorieRepository categorieRepository,
            sn.ssi.veille.services.ClusteringService clusteringService,
            sn.ssi.veille.services.ContentExtractionService contentExtractionService) {
        this.sourceRepository = sourceRepository;
        this.articleRepository = articleRepository;
        this.webClient = webClientBuilder.build();
        this.aiService = aiService;
        this.crossReferenceService = crossReferenceService;
        this.categorieRepository = categorieRepository;
        this.clusteringService = clusteringService;
        this.contentExtractionService = contentExtractionService;
    }

    // ... existing methods ...

    /**
     * Sauvegarde uniquement les nouveaux articles (évite les doublons)
     */
    private List<Article> saveNewArticles(List<Article> articles, Source source) {
        List<Article> newArticles = new ArrayList<>();

        for (Article article : articles) {
            // Nettoyage de sécurité (XSS)
            article.setTitre(sanitizeContent(article.getTitre()));
            article.setContenu(sanitizeContent(article.getContenu()));

            // Vérifier si l'article existe déjà par URL
            if (!articleRepository.existsByUrlOrigine(article.getUrlOrigine())) {

                // TENTATIVE D'EXTRACTION COMPLETE DU CONTENU
                String rssDescription = article.getContenu(); // Garder le résumé RSS original
                try {
                    String fullContent = contentExtractionService.extractFullContent(article.getUrlOrigine()).join();
                    if (fullContent != null && fullContent
                            .length() > (article.getContenu() != null ? article.getContenu().length() : 0)) {
                        String cleanText = sanitizeContent(fullContent);

                        // AI Premium Cleaning (Fix encoding & boilerplate)
                        if (aiService.isAvailable()) {
                            try {
                                log.debug("Nettoyage IA en cours pour {}...", article.getTitre());
                                cleanText = aiService.cleanContent(cleanText).join();
                            } catch (Exception e) {
                                log.warn("Echec nettoyage IA (fallback regex): {}", e.getMessage());
                            }
                        }

                        // Sécurité finale : ne jamais écraser avec du vide
                        if (cleanText != null && !cleanText.isBlank()) {
                            article.setContenu(cleanText);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Echec extraction contenu pour {}: {}", article.getUrlOrigine(), e.getMessage());
                }

                // FALLBACK RSS : Si contenu toujours vide, garder le résumé RSS
                if ((article.getContenu() == null || article.getContenu().isBlank())
                        && rssDescription != null && !rssDescription.isBlank()) {
                    article.setContenu(rssDescription);
                    log.info("📝 Fallback RSS pour {} (résumé conservé)", article.getTitre());
                }

                Article saved = articleRepository.save(article);
                newArticles.add(saved);
            }
        }

        // Enrichissement IA (Post-traitement)
        if (!newArticles.isEmpty() && aiService.isAvailable()) {
            log.info("Enrichissement IA de {} articles...", newArticles.size());
            for (Article article : newArticles) {
                try {
                    // 1. Enrichissement séquentiel (Catégories, Tags, Gravité)
                    Article enriched = aiService.enrichArticle(article).join();
                    articleRepository.save(enriched);

                    // 2. Clustering (Calcul Vectoriel + Story Grouping)
                    Article clustered = enriched;
                    try {
                        clustered = clusteringService.processClustering(enriched).join();
                    } catch (Exception e) {
                        log.error("⚠️ Clustering failed for {}: {}", enriched.getId(), e.getMessage());
                    }

                    // 3. Cross-referencing (Corrélation par Tags)
                    try {
                        crossReferenceService.processCorrelations(clustered);
                    } catch (Exception e) {
                        log.error("⚠️ Cross-ref failed for {}: {}", clustered.getId(), e.getMessage());
                    }
                } catch (Exception e) {
                    log.error("Erreur enrichment article {}: {}", article.getId(), e.getMessage());
                }
            }
        }

        log.info("Source {} : {}/{} nouveaux articles sauvegardés",
                source.getNomSource(), newArticles.size(), articles.size());
        return newArticles;
    }

    // ... existing methods ...

    @Override
    public String categorizeArticle(Article article) {
        if (!aiService.isAvailable()) {
            return "Non catégorisé (IA indisponible)";
        }

        try {
            Article enriched = aiService.enrichArticle(article).join();
            if (enriched.getCategorieId() != null) {
                return categorieRepository.findById(enriched.getCategorieId())
                        .map(Categorie::getNomCategorie)
                        .orElse("Catégorie inconnue (ID: " + enriched.getCategorieId() + ")");
            }
        } catch (Exception e) {
            log.error("Erreur classification IA: {}", e.getMessage());
        }
        return "Non catégorisé";
    }

    @Override
    public int scrapeAllSources() {
        log.info("Démarrage du scraping de toutes les sources actives");

        List<Source> activeSources = sourceRepository.findByActiveTrue();
        int totalArticles = 0;

        for (Source source : activeSources) {
            // Validation pré-scraping
            if (source.getNomSource() == null || source.getMethodeCollecte() == null || source.getUrl() == null) {
                log.warn("Source ignorée (Données invalides) : ID={}", source.getId());
                continue;
            }

            try {
                List<Article> articles = scrapeSource(source.getId());
                totalArticles += articles.size();

                // Mise à jour des stats
                source.setDerniereSyncro(LocalDateTime.now());
                source.setArticlesLastSync(articles.size());
                source.setTotalArticlesCollected(source.getTotalArticlesCollected() + articles.size());
                source.setConsecutiveFailures(0);
                source.setNextSyncAt(LocalDateTime.now().plusMinutes(source.getFrequenceScraping()));
                sourceRepository.save(source);

                log.info("Source {} : {} articles collectés", source.getNomSource(), articles.size());

                // BEST PRACTICE: Pause aléatoire (2-5s) pour respecter les serveurs cibles
                // (Politeness)
                try {
                    long delay = 2000 + (long) (Math.random() * 3000);
                    log.debug("Pause de {} ms (Politeness)...", delay);
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

            } catch (Exception e) {
                handleSourceError(source, e);
            }
        }

        log.info("Scraping terminé : {} articles collectés au total", totalArticles);
        return totalArticles;
    }

    @Override
    public List<Article> scrapeSource(String sourceId) {
        Source source = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Source non trouvée: " + sourceId));

        return switch (source.getMethodeCollecte()) {
            case RSS -> scrapeViaRss(source);
            case API -> scrapeViaApi(source);
            case SCRAPING -> scrapeViaHtml(source);
            case PLAYWRIGHT -> scrapeViaHtml(source); // Géré par la même méthode
        };
    }

    @Override
    public List<Article> scrapeViaApi(Source source) {
        log.info("Scraping via API : {}", source.getNomSource());
        List<Article> articles = new ArrayList<>();

        try {
            // Hacker News API (Firebase)
            if (source.getUrl().contains("ycombinator") || source.getUrl().contains("hacker-news")) {
                articles = scrapeHackerNews(source);
            }
            // NIST NVD API
            else if (source.getUrl().contains("nvd.nist.gov")) {
                articles = scrapeNistNvd(source);
            }
            // API générique
            else {
                log.warn("API non supportée pour : {}", source.getUrl());
            }
        } catch (Exception e) {
            log.error("Erreur scraping API {}: {}", source.getNomSource(), e.getMessage());
            throw new RuntimeException("Erreur scraping API", e);
        }

        return saveNewArticles(articles, source);
    }

    /**
     * Scraping via flux RSS (CERT-FR, BleepingComputer, The Hacker News, etc.)
     */
    public List<Article> scrapeViaRss(Source source) {
        log.info("Scraping via RSS : {}", source.getNomSource());
        List<Article> articles = new ArrayList<>();

        try {
            URL feedUrl = URI.create(source.getUrl()).toURL();

            // Configuration pour Reddit (User-Agent requis)
            java.net.URLConnection conn = feedUrl.openConnection();
            conn.setRequestProperty("User-Agent", "VeillePlateforme/1.0");

            SyndFeedInput input = new SyndFeedInput();
            // Utilisation d'InputStream pour éviter le constructeur déprécié
            // XmlReader(URLConnection)
            SyndFeed feed = input.build(new XmlReader(conn.getInputStream()));

            int count = 0;
            for (SyndEntry entry : feed.getEntries()) {
                if (count >= source.getMaxArticlesPerSync())
                    break;

                Article article = Article.builder()
                        .titre(entry.getTitle())
                        .contenu(entry.getDescription() != null ? entry.getDescription().getValue() : "")
                        .urlOrigine(entry.getLink())
                        .sourceId(source.getId())
                        .categorieId(source.getCategorieParDefaut())
                        .datePublication(entry.getPublishedDate() != null
                                ? entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                                : LocalDateTime.now())
                        .tags(new String[] {})
                        .gravite(Gravite.INFORMATION)
                        .build();

                articles.add(article);
                count++;
            }

            log.info("RSS {} : {} entrées trouvées", source.getNomSource(), articles.size());
        } catch (Exception e) {
            log.error("Erreur parsing RSS {}: {}", source.getUrl(), e.getMessage());
            throw new RuntimeException("Erreur parsing RSS", e);
        }

        return saveNewArticles(articles, source);
    }

    /**
     * Scraping via HTML (à implémenter plus tard avec Jsoup)
     */
    /**
     * Scraping via HTML avec Microsoft Playwright (Headless Browser)
     */
    public List<Article> scrapeViaHtml(Source source) {
        log.info("Scraping via Playwright (HTML) : {}", source.getNomSource());
        List<Article> articles = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            // Timeouts configurés (défaut 30s)
            page.setDefaultTimeout(source.getTimeout() * 1000);

            log.debug("Navigation vers {}", source.getUrl());
            page.navigate(source.getUrl());
            try {
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            } catch (Exception e) {
                log.warn("Timeout attente DOM pour {}, on continue...", source.getNomSource());
            }

            // Sélecteurs par défaut si non configurés
            String titleSelector = source.getSelectorTitle() != null ? source.getSelectorTitle()
                    : "article h2 a, .post-title a, h2 a";

            List<ElementHandle> elements = page.querySelectorAll(titleSelector);
            log.info("Playwright a trouvé {} éléments avec le sélecteur '{}'", elements.size(), titleSelector);

            int count = 0;
            for (ElementHandle element : elements) {
                if (count >= source.getMaxArticlesPerSync())
                    break;

                try {
                    String title = element.innerText();
                    String link = element.getAttribute("href");

                    if (link != null && !link.isEmpty()) {
                        if (!link.startsWith("http")) {
                            URI baseUri = URI.create(source.getUrl());
                            link = baseUri.resolve(link).toString();
                        }

                        Article article = Article.builder()
                                .titre(title)
                                .urlOrigine(link)
                                .sourceId(source.getId())
                                .categorieId(source.getCategorieParDefaut())
                                .datePublication(LocalDateTime.now())
                                .gravite(Gravite.INFORMATION)
                                .tags(new String[] { "Scraped", "Playwright" })
                                .build();

                        // Si nécessaire, on pourrait visiter l'article ici pour le contenu complet
                        article.setContenu("Contenu extrait via Playwright (Index only)");

                        articles.add(article);
                        count++;
                    }
                } catch (Exception e) {
                    log.warn("Erreur extraction élément Playwright: {}", e.getMessage());
                }
            }

            browser.close();
        } catch (Exception e) {
            log.error("Erreur Playwright {}: {}", source.getNomSource(), e.getMessage());
            // Ne pas tout bloquer, retourner la liste vide ou partielle
        }

        return saveNewArticles(articles, source);
    }

    /**
     * Scrape Hacker News via Firebase API
     */
    private List<Article> scrapeHackerNews(Source source) {
        List<Article> articles = new ArrayList<>();

        try {
            // Récupérer les top stories IDs
            Integer[] storyIds = webClient.get()
                    .uri("https://hacker-news.firebaseio.com/v0/topstories.json")
                    .retrieve()
                    .bodyToMono(Integer[].class)
                    .block();

            if (storyIds == null)
                return articles;

            int limit = Math.min(source.getMaxArticlesPerSync(), storyIds.length);

            for (int i = 0; i < limit; i++) {
                try {
                    HackerNewsItem item = webClient.get()
                            .uri("https://hacker-news.firebaseio.com/v0/item/" + storyIds[i] + ".json")
                            .retrieve()
                            .bodyToMono(HackerNewsItem.class)
                            .block();

                    if (item != null && item.url() != null) {
                        Article article = Article.builder()
                                .titre(item.title())
                                .contenu("Score: " + item.score() + " | Commentaires: " + item.descendants())
                                .urlOrigine(item.url())
                                .sourceId(source.getId())
                                .categorieId(source.getCategorieParDefaut())
                                .datePublication(LocalDateTime.now())
                                .tags(new String[] {})
                                .gravite(Gravite.INFORMATION)
                                .build();

                        articles.add(article);
                    }
                } catch (Exception e) {
                    log.debug("Erreur item HN {}: {}", storyIds[i], e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Erreur Hacker News API: {}", e.getMessage());
        }

        return articles;
    }

    /**
     * Scrape NIST NVD via API REST (Sans clé API = Rate limit plus strict)
     */
    private List<Article> scrapeNistNvd(Source source) {
        log.info("Scraping NIST NVD...");
        List<Article> articles = new ArrayList<>();

        try {
            // Récupérer les CVEs récentes
            // Format NIST attendu: YYYY-MM-DDTHH:mm:ss.SSS
            // Pour simplifier, on prend juste les derniers publiés sans filtre de date
            // complexe pour l'instant

            NistResponse response = webClient.get()
                    .uri("https://services.nvd.nist.gov/rest/json/cves/2.0?resultsPerPage="
                            + source.getMaxArticlesPerSync())
                    .retrieve()
                    .bodyToMono(NistResponse.class)
                    .block();

            if (response != null && response.vulnerabilities() != null) {
                for (NistVulnerability item : response.vulnerabilities()) {
                    Cve cve = item.cve();
                    String description = cve.descriptions().stream()
                            .filter(d -> "en".equals(d.lang()))
                            .findFirst()
                            .map(d -> d.value())
                            .orElse("Pas de description");

                    Article article = Article.builder()
                            .titre(cve.id())
                            .contenu(description)
                            .urlOrigine("https://nvd.nist.gov/vuln/detail/" + cve.id())
                            .sourceId(source.getId())
                            .categorieId(source.getCategorieParDefaut())
                            .datePublication(
                                    cve.published() != null ? LocalDateTime.parse(cve.published().substring(0, 19))
                                            : LocalDateTime.now())
                            .tags(new String[] { "CVE", "Vulnerability" })
                            .gravite(determineNistGravity(cve))
                            .build();

                    articles.add(article);
                }
            }
        } catch (Exception e) {
            log.error("Erreur NIST API: {}", e.getMessage());
        }

        return articles;
    }

    private Gravite determineNistGravity(Cve cve) {
        if (cve.metrics() != null && cve.metrics().cvssMetricV31() != null
                && !cve.metrics().cvssMetricV31().isEmpty()) {
            Double score = cve.metrics().cvssMetricV31().get(0).cvssData().baseScore();
            if (score >= 9.0)
                return Gravite.CRITIQUE;
            if (score >= 7.0)
                return Gravite.ELEVE;
            if (score >= 4.0)
                return Gravite.IMPORTANT;
            return Gravite.AVIS;
        }
        return Gravite.INFORMATION;
    }

    // Records pour NIST API
    record NistResponse(List<NistVulnerability> vulnerabilities) {
    }

    record NistVulnerability(Cve cve) {
    }

    record Cve(String id, String published, List<NistDescription> descriptions, NistMetrics metrics) {
    }

    record NistDescription(String lang, String value) {
    }

    record NistMetrics(List<CvssV31> cvssMetricV31) {
    }

    record CvssV31(CvssData cvssData) {
    }

    record CvssData(Double baseScore) {
    }

    /**
     * Gestion des erreurs de source avec Exponential Backoff
     */
    private void handleSourceError(Source source, Exception e) {
        source.setLastError(e.getMessage());
        source.setLastErrorAt(LocalDateTime.now());
        int failures = source.getConsecutiveFailures() + 1;
        source.setConsecutiveFailures(failures);

        // Exponential Backoff : on attend plus longtemps à chaque échec
        // Echec 1: +60m, Echec 2: +120m, Echec 3: +180m...
        int backoffMinutes = source.getFrequenceScraping() * Math.min(failures, 10);
        source.setNextSyncAt(LocalDateTime.now().plusMinutes(backoffMinutes));

        // Désactiver uniquement si > 10 échecs (plus tolérant)
        if (failures >= 10) {
            source.setActive(false);
            log.error("Source {} DÉSACTIVÉE après {} échecs consécutifs", source.getNomSource(), failures);
        } else {
            log.warn("Source {} en erreur ({}/10). Prochain essai dans {}min. Cause: {}",
                    source.getNomSource(), failures, backoffMinutes, e.getMessage());
        }

        sourceRepository.save(source);
    }

    /**
     * Nettoie le contenu HTML pour éviter les failles XSS simples.
     */
    private String sanitizeContent(String content) {
        if (content == null)
            return "";
        return content.replaceAll("<script.*?>.*?</script>", "") // Retire les scripts
                .replaceAll("javascript:", ""); // Retire les appels JS
    }

    @Override
    public Gravite determineGravity(Article article) {
        String content = (article.getTitre() + " " + article.getContenu()).toLowerCase();

        if (content.contains("critical") || content.contains("critique") || content.contains("zero-day")) {
            return Gravite.CRITIQUE;
        } else if (content.contains("high") || content.contains("grave") || content.contains("ransomware")) {
            return Gravite.ELEVE;
        } else if (content.contains("medium") || content.contains("moyen") || content.contains("vulnerability")) {
            return Gravite.IMPORTANT;
        } else if (content.contains("low") || content.contains("faible")) {
            return Gravite.AVIS;
        }

        return Gravite.INFORMATION;
    }

    @Override
    public ScrapingHealthReport getHealthReport() {
        List<Source> allSources = sourceRepository.findAll();
        List<Source> activeSources = sourceRepository.findByActiveTrue();

        List<ScrapingHealthReport.SourceStatus> statuses = allSources.stream()
                .map(s -> new ScrapingHealthReport.SourceStatus(
                        s.getId(),
                        s.getNomSource(),
                        s.isActive() && s.getConsecutiveFailures() < 3,
                        s.getLastError(),
                        s.getDerniereSyncro()))
                .toList();

        long totalArticles = allSources.stream()
                .mapToLong(Source::getTotalArticlesCollected)
                .sum();

        int failedCount = (int) allSources.stream()
                .filter(s -> s.getConsecutiveFailures() > 0)
                .count();

        return new ScrapingHealthReport(
                activeSources.size(),
                (int) totalArticles,
                failedCount,
                LocalDateTime.now(),
                statuses);
    }

    @Override
    public Article testAI(String content) {
        log.info("Test IA demandé avec contenu: {}", content);
        Article dummy = Article.builder()
                .titre("Test AI")
                .contenu(content)
                .sourceId("TEST")
                .urlOrigine("test://ai")
                .datePublication(LocalDateTime.now())
                .build();

        return aiService.enrichArticle(dummy).join();
    }

    /**
     * Record pour parser les réponses Hacker News
     */
    private record HackerNewsItem(
            int id,
            String title,
            String url,
            int score,
            int descendants) {
    }
}
