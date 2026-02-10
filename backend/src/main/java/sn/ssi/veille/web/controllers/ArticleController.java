package sn.ssi.veille.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.*;

@RequestMapping("/api/v1/articles")
public interface ArticleController {
    @GetMapping
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getLatestArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @GetMapping("/{id}")
    ResponseEntity<ArticleResponse> getArticleById(
            @PathVariable String id);

    @GetMapping("/{id}/related")
    ResponseEntity<java.util.List<ArticleSummaryResponse>> getRelatedArticles(
            @PathVariable String id);

    @PostMapping("/search")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> searchArticles(
            @RequestBody ArticleSearchCriteria criteria);

    @GetMapping("/categorie/{categorieId}")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesByCategorie(
            @PathVariable String categorieId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @GetMapping("/source/{sourceId}")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesBySource(
            @PathVariable String sourceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @GetMapping("/gravite/{gravite}")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesByGravite(
            @PathVariable Gravite gravite,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @GetMapping("/trending")
    ResponseEntity<PageResponse<ArticleSummaryResponse>> getTrendingArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size);

    @GetMapping("/weekly-summary")
    ResponseEntity<WeeklySummaryResponse> getWeeklySummary();

    // ==================== ENDPOINTS ADMIN ====================

    @PostMapping
    ResponseEntity<ArticleResponse> createArticle(
            @Valid @RequestBody ArticleRequest request);

    @PutMapping("/{id}")
    ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable String id,
            @Valid @RequestBody ArticleRequest request);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteArticle(
            @PathVariable String id);

    @PostMapping("/{id}/summarize")
    ResponseEntity<MessageResponse> generateAISummary(
            @PathVariable String id,
            @RequestParam(required = false) String apiKey);
}
