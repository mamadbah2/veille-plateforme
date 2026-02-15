package sn.ssi.veille.services.implementation;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.Proxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sn.ssi.veille.services.ContentExtractionService;
import sn.ssi.veille.services.implementation.ProxyRotatorService.ProxyInfo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class JsoupContentExtractionService implements ContentExtractionService {

    private static final int TIMEOUT_MS = 15000;

    private final ProxyRotatorService proxyRotator;

    @Override
    @Async
    public CompletableFuture<String> extractFullContent(String url) {
        // Stratégie spécifique pour Reddit via API JSON (contournement antibot)
        if (url.contains("reddit.com")) {
            String jsonContent = extractFromRedditJson(url);
            if (jsonContent != null && !jsonContent.isEmpty()) {
                return CompletableFuture.completedFuture(cleanText(jsonContent));
            }
        }

        String userAgent = proxyRotator.getRandomUserAgent();

        // === ÉTAPE 1 : Jsoup SANS proxy (rapide, léger) ===
        try {
            String result = extractWithJsoup(url, userAgent, null);
            if (result != null && result.length() >= 300) {
                return CompletableFuture.completedFuture(result);
            }
            log.debug("Jsoup direct insuffisant ({} chars) pour {}", result != null ? result.length() : 0, url);
        } catch (Exception e) {
            log.debug("Jsoup direct échoué pour {} : {}", url, e.getMessage());
        }

        // === ÉTAPE 2 : Jsoup AVEC proxy rotatif ===
        if (proxyRotator.hasProxies()) {
            ProxyInfo proxy = proxyRotator.getNext();
            if (proxy != null) {
                try {
                    log.info("🔄 Tentative Jsoup+Proxy ({}) pour {}", proxy.key(), url);
                    String result = extractWithJsoup(url, userAgent, proxy);
                    if (result != null && result.length() >= 300) {
                        return CompletableFuture.completedFuture(result);
                    }
                    proxyRotator.markBad(proxy); // Proxy pas efficace
                } catch (Exception e) {
                    proxyRotator.markBad(proxy);
                    log.debug("Jsoup+Proxy échoué pour {} : {}", url, e.getMessage());
                }
            }
        }

        // === ÉTAPE 3 : Playwright AVEC proxy (navigateur complet) ===
        log.info("🎭 Fallback Playwright pour {}", url);
        String playwrightResult = extractWithPlaywright(url, userAgent);
        if (playwrightResult != null && !playwrightResult.isBlank()) {
            return CompletableFuture.completedFuture(playwrightResult);
        }

        // === ÉTAPE 4 : Tout a échoué ===
        log.warn("❌ Extraction impossible pour {} (toutes stratégies épuisées)", url);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Extraction via Jsoup, avec ou sans proxy.
     */
    private String extractWithJsoup(String url, String userAgent, ProxyInfo proxy) throws IOException {
        var connection = Jsoup.connect(url)
                .userAgent(userAgent)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Charset", "utf-8") // Request UTF-8 explicitly
                .header("DNT", "1")
                .header("Upgrade-Insecure-Requests", "1")
                .timeout(TIMEOUT_MS)
                .followRedirects(true);

        // Appliquer le proxy si disponible
        if (proxy != null) {
            connection.proxy(proxy.host(), proxy.port());
        }

        // Force execution to handle charset manually if needed
        org.jsoup.Connection.Response response = connection.execute();

        // Fix encoding: if charset is not explicitly set by server, assume UTF-8
        // (common modern web)
        // or check for "ISO-8859-1" default which is often wrong for modern sites
        if (response.charset() == null || response.charset().equalsIgnoreCase("ISO-8859-1")) {
            response.charset("UTF-8");
        }

        Document doc = response.parse();

        // Nettoyage initial (supprimer les éléments inutiles)
        doc.select(
                "script, style, nav, footer, header, aside, .ads, .comments, iframe, .share-buttons, .newsletter, .cookie-banner, .popup")
                .remove();

        // Recherche du conteneur principal (Heuristique Readability-lite)
        Element mainContent = findBestContentContainer(doc);
        String text = (mainContent != null) ? mainContent.wholeText() : doc.body().wholeText();

        text = cleanText(text);

        // Vérification de validité
        if (text.contains("Access Denied") || text.contains("404 Not Found") || text.contains("Just a moment")) {
            return null; // Cloudflare challenge page
        }

        return text;
    }

    /**
     * Extraction via l'API publique JSON de Reddit.
     * Contourne le blocage HTML/JS en récupérant les données brutes.
     */
    private String extractFromRedditJson(String originalUrl) {
        try {
            // Construction de l'URL JSON : retire les paramètres et ajoute .json
            String jsonUrl = originalUrl.split("\\?")[0];
            if (jsonUrl.endsWith("/")) {
                jsonUrl = jsonUrl.substring(0, jsonUrl.length() - 1);
            }
            jsonUrl += ".json";

            log.info("Tentative extraction Reddit via API JSON : {}", jsonUrl);

            // Appel HTTP simple (Jsoup ignoreContentType pour récupérer le JSON)
            org.jsoup.Connection.Response response = Jsoup.connect(jsonUrl)
                    .userAgent(proxyRotator.getRandomUserAgent())
                    .ignoreContentType(true)
                    .execute();

            // Force UTF-8 for JSON content (Reddit API is UTF-8)
            response.charset("UTF-8");
            String jsonResponse = response.body();

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(jsonResponse);

            if (!root.isArray() || root.size() < 1)
                return null;

            StringBuilder content = new StringBuilder();

            // 1. Le Post (Titre + Selftext)
            com.fasterxml.jackson.databind.JsonNode postData = root.get(0).path("data").path("children").get(0)
                    .path("data");
            String title = postData.path("title").asText();
            String selftext = postData.path("selftext").asText();

            content.append(title).append("\n\n");
            content.append(selftext).append("\n\n");
            content.append("--- Commentaires ---\n\n");

            // 2. Les Commentaires (Top limités)
            if (root.size() > 1) {
                com.fasterxml.jackson.databind.JsonNode comments = root.get(1).path("data").path("children");
                int limit = 0;
                for (com.fasterxml.jackson.databind.JsonNode comment : comments) {
                    if (limit++ > 10)
                        break; // Limite aux 10 premiers top commentaires
                    String body = comment.path("data").path("body").asText();
                    if (body != null && !body.isEmpty() && !body.equals("null")) {
                        content.append("- ").append(body).append("\n\n");
                    }
                }
            }

            return content.toString().trim();

        } catch (Exception e) {
            log.warn("Echec extraction API Reddit JSON pour {} : {}", originalUrl, e.getMessage());
            return null;
        }
    }

    /**
     * Fallback : Extraction via Playwright (Headless Browser) pour les sites
     * dynamiques/protégés. Utilise un proxy rotatif si disponible.
     */
    private String extractWithPlaywright(String url, String userAgent) {
        try (Playwright playwright = Playwright.create()) {

            var launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(true)
                    .setArgs(java.util.List.of(
                            "--disable-blink-features=AutomationControlled",
                            "--disable-dev-shm-usage",
                            "--no-sandbox"));

            // Configurer proxy Playwright si disponible
            ProxyInfo proxy = proxyRotator.getNext();
            if (proxy != null) {
                launchOptions.setProxy(new Proxy("http://" + proxy.key()));
                log.info("🎭 Playwright avec proxy {} pour {}", proxy.key(), url);
            }

            Browser browser = playwright.chromium().launch(launchOptions);

            // Context avec User-Agent réaliste
            com.microsoft.playwright.BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent(userAgent)
                    .setViewportSize(1920, 1080)
                    .setLocale("en-US"));

            Page page = context.newPage();

            // Bloquer les ressources lourdes (images, CSS, fonts) pour aller plus vite
            page.route("**/*.{png,jpg,jpeg,gif,svg,css,woff,woff2,ttf}", route -> route.abort());

            // Timeout plus généreux pour le chargement complet
            page.setDefaultTimeout(30000);

            log.debug("Playwright navigation vers {}", url);
            page.navigate(url);

            // Tenter d'attendre que le réseau soit calme
            try {
                page.waitForLoadState(LoadState.NETWORKIDLE);
                Thread.sleep(1500); // Wait a bit more for rendering
            } catch (Exception e) {
                log.debug("Playwright waitForLoadState timeout, tentative d'extraction quand même.");
            }

            // Récupérer le HTML final rendu par JS
            String html = page.content();
            log.info("Playwright a récupéré {} caractères HTML avant nettoyage pour {}", html.length(), url);

            Document doc = Jsoup.parse(html);

            // Nettoyage et Extraction (même logique que Jsoup)
            doc.select(
                    "script, style, nav, footer, header, aside, .ads, .comments, iframe, .share-buttons, .newsletter, .cookie-banner, .popup")
                    .remove();
            Element mainContent = findBestContentContainer(doc);

            String text = (mainContent != null) ? mainContent.wholeText() : doc.body().wholeText();

            browser.close();

            text = cleanText(text);

            // Vérifier si c'est une page challenge Cloudflare
            if (text.contains("Just a moment") || text.contains("Checking your browser") || text.length() < 200) {
                if (proxy != null)
                    proxyRotator.markBad(proxy);
                log.warn("Playwright bloqué par Cloudflare pour {}", url);
                return null;
            }

            return text;

        } catch (Exception e) {
            log.error("Playwright a aussi échoué pour {}: {}", url, e.getMessage());
            return null;
        }
    }

    /**
     * Nettoie le texte brut pour le rendre lisible :
     * - Remplace les multiples sauts de ligne par un seul double saut
     * - Supprime les espaces insécables et tabulations excessives
     * - Retire les lignes "parasites" communes (Share, Read more...)
     */
    private String cleanText(String text) {
        if (text == null)
            return "";

        // 1. Normalisation des espaces (mais garde les sauts de ligne)
        String cleaned = text.replaceAll("[\\t\\u00A0]+", " ");

        // 2. Gestion des sauts de ligne
        cleaned = cleaned.replaceAll("\\n\\s*\\n\\s*\\n+", "\n\n");

        // 3. Suppression des lignes vides ou avec juste des espaces
        cleaned = cleaned.replaceAll("(?m)^[ \\t]+$", "");

        // 4. Alignement à gauche
        cleaned = cleaned.replaceAll("(?m)^[ \\t]+", "");

        // 5. Filtrage de phrases parasites courantes
        cleaned = cleaned.replaceAll("(?i)(read more|continue reading|also read|share this post).*", "");

        return cleaned.trim();
    }

    /**
     * Tente d'identifier le meilleur conteneur pour le contenu de l'article.
     */
    private Element findBestContentContainer(Document doc) {
        // Priorité 1 : Balises sémantiques
        Element article = doc.selectFirst("article");
        if (article != null)
            return article;

        Element main = doc.selectFirst("main");
        if (main != null)
            return main;

        // Priorité 2 : ID ou Classes communs
        Element commonId = doc
                .selectFirst("#content, #main, .post-content, .article-content, .entry-content, .article-body");
        if (commonId != null)
            return commonId;

        // Priorité 3 : Le div qui contient le plus de paragraphes
        Elements divs = doc.select("div");
        Element bestDiv = null;
        int maxParagraphs = 0;

        for (Element div : divs) {
            int pCount = div.select("p").size();
            if (pCount > maxParagraphs) {
                maxParagraphs = pCount;
                bestDiv = div;
            }
        }

        return bestDiv;
    }
}
