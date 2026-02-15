package sn.ssi.veille.web.controllers.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.services.implementation.ArticleServiceImpl;
import sn.ssi.veille.web.controllers.ArticleController;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.*;

/**
 * Implémentation du contrôleur Article.
 * Expose les endpoints REST pour la gestion des articles.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@RestController
public class ArticleControllerImpl implements ArticleController {

    private final ArticleServiceImpl articleService;
    private final sn.ssi.veille.services.AIService aiService;
    private final sn.ssi.veille.services.ContentExtractionService contentExtractionService;
    private final sn.ssi.veille.services.ScrapingService scrapingService;

    public ArticleControllerImpl(ArticleServiceImpl articleService, sn.ssi.veille.services.AIService aiService,
            sn.ssi.veille.services.ContentExtractionService contentExtractionService,
            sn.ssi.veille.services.ScrapingService scrapingService) {
        this.articleService = articleService;
        this.aiService = aiService;
        this.contentExtractionService = contentExtractionService;
        this.scrapingService = scrapingService;
    }

    @Override
    public ResponseEntity<PageResponse<ArticleSummaryResponse>> getLatestArticles(int page, int size) {
        return ResponseEntity.ok(articleService.getLatestArticles(page, size));
    }

    @Override
    public ResponseEntity<sn.ssi.veille.models.entities.Article> verifyArticle(String id) {
        return ResponseEntity.ok(articleService.getArticleEntityById(id));
    }

    @Override
    public ResponseEntity<ArticleResponse> getArticleById(String id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @Override
    public ResponseEntity<java.util.List<ArticleSummaryResponse>> getRelatedArticles(String id) {
        return ResponseEntity.ok(articleService.getRelatedArticles(id));
    }

    @Override
    public ResponseEntity<PageResponse<ArticleSummaryResponse>> searchArticles(ArticleSearchCriteria criteria) {
        return ResponseEntity.ok(articleService.searchArticles(criteria));
    }

    @Override
    public ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesByCategorie(String categorieId, int page,
            int size) {
        return ResponseEntity.ok(articleService.getArticlesByCategorie(categorieId, page, size));
    }

    @Override
    public ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesBySource(String sourceId, int page,
            int size) {
        return ResponseEntity.ok(articleService.getArticlesBySource(sourceId, page, size));
    }

    @Override
    public ResponseEntity<PageResponse<ArticleSummaryResponse>> getArticlesByGravite(Gravite gravite, int page,
            int size) {
        return ResponseEntity.ok(articleService.getArticlesByGravite(gravite, page, size));
    }

    @Override
    public ResponseEntity<PageResponse<ArticleSummaryResponse>> getTrendingArticles(int page, int size) {
        return ResponseEntity.ok(articleService.getTrendingArticles(page, size));
    }

    @Override
    public ResponseEntity<WeeklySummaryResponse> getWeeklySummary() {
        return ResponseEntity.ok(articleService.getWeeklySummary());
    }

    @Override
    public ResponseEntity<ArticleResponse> createArticle(ArticleRequest request) {
        ArticleResponse created = articleService.createArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<ArticleResponse> updateArticle(String id, ArticleRequest request) {
        return ResponseEntity.ok(articleService.updateArticle(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteArticle(String id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<sn.ssi.veille.web.dto.responses.MessageResponse> generateAISummary(String id, String apiKey) {
        String summary = articleService.generateAISummary(id, apiKey);
        return ResponseEntity.ok(sn.ssi.veille.web.dto.responses.MessageResponse.success(summary));
    }

    @Override
    public ResponseEntity<java.util.List<Double>> testEmbedding(java.util.Map<String, String> payload) {
        String text = payload.getOrDefault("text", "Test embedding");
        var vector = aiService.getEmbeddings(text).join();
        return ResponseEntity.ok(vector);
    }

    @Override
    public ResponseEntity<String> testContentExtraction(String url) {
        String content = contentExtractionService.extractFullContent(url).join();
        return ResponseEntity.ok(content);
    }

    @Override
    public ResponseEntity<Integer> syncAllSources() {
        int count = scrapingService.scrapeAllSources();
        return ResponseEntity.ok(count);
    }
}
