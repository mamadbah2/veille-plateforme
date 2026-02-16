package sn.ssi.veille.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.services.ScrapingService;
import sn.ssi.veille.services.ScrapingService.ScrapingHealthReport;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour les opérations de scraping.
 */
@RestController
@RequestMapping("/api/v1/scraping")
@Tag(name = "Scraping", description = "Opérations de collecte d'articles")
public class ScrapingController {

    private final ScrapingService scrapingService;

    public ScrapingController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @PostMapping("/run")
    @Operation(summary = "Lancer le scraping de toutes les sources actives")
    public ResponseEntity<Map<String, Object>> scrapeAllSources() {
        int count = scrapingService.scrapeAllSources();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Scraping terminé",
                "articlesCollected", count));
    }

    @PostMapping("/sources/{sourceId}")
    @Operation(summary = "Lancer le scraping d'une source spécifique")
    public ResponseEntity<Map<String, Object>> scrapeSource(@PathVariable String sourceId) {
        List<Article> articles = scrapingService.scrapeSource(sourceId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "sourceId", sourceId,
                "articlesCollected", articles.size()));
    }

    @GetMapping("/health")
    @Operation(summary = "Rapport de santé du système de scraping")
    public ResponseEntity<ScrapingHealthReport> getHealthReport() {
        return ResponseEntity.ok(scrapingService.getHealthReport());
    }

    @PostMapping("/test-ai")
    @Operation(summary = "Tester l'intégration IA avec un contenu arbitraire")
    public ResponseEntity<Article> testAI(@RequestBody String content) {
        return ResponseEntity.ok(scrapingService.testAI(content));
    }
}
