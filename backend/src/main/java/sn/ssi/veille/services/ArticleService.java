package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.ArticleResponse;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;
import sn.ssi.veille.web.dto.responses.WeeklySummaryResponse;

public interface ArticleService {

    ArticleResponse createArticle(ArticleRequest request);

    ArticleResponse getArticleById(String id);

    sn.ssi.veille.models.entities.Article getArticleEntityById(String id); // For verification only

    PageResponse<ArticleSummaryResponse> getLatestArticles(int page, int size);

    PageResponse<ArticleSummaryResponse> searchArticles(ArticleSearchCriteria criteria);

    PageResponse<ArticleSummaryResponse> getArticlesByCategorie(String categorieId, int page, int size);

    PageResponse<ArticleSummaryResponse> getArticlesBySource(String sourceId, int page, int size);

    PageResponse<ArticleSummaryResponse> getTrendingArticles(int page, int size);

    ArticleResponse updateArticle(String id, ArticleRequest request);

    void deleteArticle(String id);

    WeeklySummaryResponse getWeeklySummary();

    String generateAISummary(String articleId, String apiKey);

    boolean articleExists(String urlOrigine, String sourceId);

    java.util.List<ArticleSummaryResponse> getRelatedArticles(String id);
}
