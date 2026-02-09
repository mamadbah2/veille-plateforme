package sn.ssi.veille.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sn.ssi.veille.exceptions.ArticleNotFoundException;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.services.ArticleService;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service Article.
 * Gère les opérations CRUD et la recherche d'articles.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final SourceRepository sourceRepository;
    private final CategorieRepository categorieRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository,
            SourceRepository sourceRepository,
            CategorieRepository categorieRepository) {
        this.articleRepository = articleRepository;
        this.sourceRepository = sourceRepository;
        this.categorieRepository = categorieRepository;
    }

    @Override
    public ArticleResponse createArticle(ArticleRequest request) {
        Article article = Article.builder()
                .titre(request.titre())
                .contenu(request.contenu())
                .resume(request.resume())
                .urlOrigine(request.urlOrigine())
                .imageUrl(request.imageUrl())
                .sourceId(request.sourceId())
                .categorieId(request.categorieId())
                .gravite(request.gravite() != null ? request.gravite() : Gravite.INFORMATION)
                .tags(request.tags())
                .auteur(request.auteur())
                .datePublication(request.datePublication() != null ? request.datePublication() : LocalDateTime.now())
                .build();

        Article saved = articleRepository.save(article);
        return toFullResponse(saved);
    }

    @Override
    public ArticleResponse getArticleById(String id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé avec l'id: " + id));

        // Incrémenter le nombre de vues
        article.setNombreVues(article.getNombreVues() + 1);
        articleRepository.save(article);

        return toFullResponse(article);
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getLatestArticles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datePublication"));
        Page<Article> articlePage = articleRepository.findAll(pageRequest);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleSummaryResponse> searchArticles(ArticleSearchCriteria criteria) {
        // TODO: Implémenter la recherche avancée avec criteria
        PageRequest pageRequest = PageRequest.of(
                criteria.page() != null ? criteria.page() : 0,
                criteria.size() != null ? criteria.size() : 20,
                Sort.by(Sort.Direction.DESC, "datePublication"));
        Page<Article> articlePage = articleRepository.findAll(pageRequest);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getArticlesByCategorie(String categorieId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datePublication"));
        Page<Article> articlePage = articleRepository.findByCategorieId(categorieId, pageRequest);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getArticlesBySource(String sourceId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datePublication"));
        Page<Article> articlePage = articleRepository.findBySourceId(sourceId, pageRequest);
        return toPageResponse(articlePage);
    }

    @Override
    public PageResponse<ArticleSummaryResponse> getTrendingArticles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "nombreVues"));
        Page<Article> articlePage = articleRepository.findAll(pageRequest);
        return toPageResponse(articlePage);
    }

    @Override
    public ArticleResponse updateArticle(String id, ArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé avec l'id: " + id));

        if (request.titre() != null)
            article.setTitre(request.titre());
        if (request.contenu() != null)
            article.setContenu(request.contenu());
        if (request.resume() != null)
            article.setResume(request.resume());
        if (request.urlOrigine() != null)
            article.setUrlOrigine(request.urlOrigine());
        if (request.imageUrl() != null)
            article.setImageUrl(request.imageUrl());
        if (request.sourceId() != null)
            article.setSourceId(request.sourceId());
        if (request.categorieId() != null)
            article.setCategorieId(request.categorieId());
        if (request.gravite() != null)
            article.setGravite(request.gravite());
        if (request.tags() != null)
            article.setTags(request.tags());
        if (request.auteur() != null)
            article.setAuteur(request.auteur());

        Article updated = articleRepository.save(article);
        return toFullResponse(updated);
    }

    @Override
    public void deleteArticle(String id) {
        if (!articleRepository.existsById(id)) {
            throw new ArticleNotFoundException("Article non trouvé avec l'id: " + id);
        }
        articleRepository.deleteById(id);
    }

    @Override
    public WeeklySummaryResponse getWeeklySummary() {
        // TODO: Implémenter le résumé hebdomadaire complet
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1);

        return new WeeklySummaryResponse(
                weekAgo,
                now,
                articleRepository.count(),
                List.of(),
                "Résumé IA non disponible",
                List.of(),
                List.of());
    }

    @Override
    public String generateAISummary(String articleId, String apiKey) {
        // TODO: Implémenter la génération IA
        return "Fonctionnalité IA non disponible pour le moment.";
    }

    @Override
    public boolean articleExists(String urlOrigine, String sourceId) {
        return articleRepository.existsByUrlOrigineAndSourceId(urlOrigine, sourceId);
    }

    // ==================== HELPERS ====================

    private ArticleResponse toFullResponse(Article article) {
        SourceResponse sourceResponse = null;
        CategorieResponse categorieResponse = null;

        if (article.getSourceId() != null) {
            Source source = sourceRepository.findById(article.getSourceId()).orElse(null);
            if (source != null) {
                sourceResponse = new SourceResponse(
                        source.getId(), source.getUrl(), source.getNomSource(),
                        source.getMethodeCollecte(), source.isActive(),
                        source.getFrequenceScraping(), source.getDerniereSyncro(), source.getCreatedAt());
            }
        }

        if (article.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(article.getCategorieId()).orElse(null);
            if (categorie != null) {
                categorieResponse = new CategorieResponse(
                        categorie.getId(), categorie.getNomCategorie(), categorie.getDescription(),
                        categorie.getCouleur(), categorie.getIcone(), categorie.getCreatedAt());
            }
        }

        return new ArticleResponse(
                article.getId(),
                article.getTitre(),
                article.getContenu(),
                article.getResume(),
                article.getUrlOrigine(),
                article.getImageUrl(),
                article.getGravite(),
                article.getTags(),
                article.getAuteur(),
                article.getNombreVues(),
                article.getDatePublication(),
                sourceResponse,
                categorieResponse,
                article.getCreatedAt());
    }

    private ArticleSummaryResponse toSummaryResponse(Article article) {
        String nomSource = null;
        String nomCategorie = null;
        String couleurCategorie = null;

        if (article.getSourceId() != null) {
            Source source = sourceRepository.findById(article.getSourceId()).orElse(null);
            if (source != null)
                nomSource = source.getNomSource();
        }

        if (article.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(article.getCategorieId()).orElse(null);
            if (categorie != null) {
                nomCategorie = categorie.getNomCategorie();
                couleurCategorie = categorie.getCouleur();
            }
        }

        return new ArticleSummaryResponse(
                article.getId(),
                article.getTitre(),
                article.getResume(),
                article.getImageUrl(),
                article.getGravite(),
                nomSource,
                nomCategorie,
                couleurCategorie,
                article.getNombreVues(),
                article.getDatePublication());
    }

    private PageResponse<ArticleSummaryResponse> toPageResponse(Page<Article> articlePage) {
        List<ArticleSummaryResponse> content = articlePage.getContent().stream()
                .map(this::toSummaryResponse)
                .toList();

        return new PageResponse<>(
                content,
                articlePage.getNumber(),
                articlePage.getSize(),
                articlePage.getTotalElements(),
                articlePage.getTotalPages(),
                articlePage.isFirst(),
                articlePage.isLast());
    }
}
