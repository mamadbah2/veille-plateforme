package sn.ssi.veille.services.implementation;

import sn.ssi.veille.services.ArticleService;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.ArticleResponse;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;
import sn.ssi.veille.web.dto.responses.WeeklySummaryResponse;

public class ArticleServiceImpl implements ArticleService {

    @Override
    public ArticleResponse createArticle(ArticleRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createArticle'");
    }

    @Override
    public ArticleResponse getArticleById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArticleById'");
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getLatestArticles(int page, int size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLatestArticles'");
    }

    @Override
    public PageResponse<ArticleSummaryResponse> searchArticles(ArticleSearchCriteria criteria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchArticles'");
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getArticlesByCategorie(String categorieId, int page, int size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArticlesByCategorie'");
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getArticlesBySource(String sourceId, int page, int size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArticlesBySource'");
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getTrendingArticles(int page, int size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTrendingArticles'");
    }

    @Override
    public ArticleResponse updateArticle(String id, ArticleRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateArticle'");
    }

    @Override
    public void deleteArticle(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteArticle'");
    }

    @Override
    public WeeklySummaryResponse getWeeklySummary() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWeeklySummary'");
    }

    @Override
    public String generateAISummary(String articleId, String apiKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateAISummary'");
    }

    @Override
    public boolean articleExists(String urlOrigine, String sourceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'articleExists'");
    }
    
}
