package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import sn.ssi.veille.exceptions.ArticleNotFoundException;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.services.implementation.ArticleServiceImpl;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.ArticleResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private SourceRepository sourceRepository;
    @Mock
    private CategorieRepository categorieRepository;
    @Mock
    private CrossReferenceService crossReferenceService;
    @Mock
    private AIService aiService;
    @Mock
    private ClusteringService clusteringService;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private Article article;
    private ArticleRequest articleRequest;

    @BeforeEach
    void setUp() {
        article = Article.builder()
                .id("art1")
                .titre("Test Article")
                .contenu("Content of test article")
                .datePublication(LocalDateTime.now())
                .gravite(Gravite.INFORMATION)
                .build();

        articleRequest = new ArticleRequest(
                "Test Article",
                "Content",
                "Summary",
                "http://test.com",
                "http://img.com",
                "source1",
                "cat1",
                Gravite.CRITIQUE,
                new String[] { "tag1" },
                "Author",
                LocalDateTime.now());
    }

    @Test
    @DisplayName("Create Article - Should Save and TriggerAsync Processes")
    void createArticle_ShouldSaveAndTriggerAsync() {
        // Given
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        when(aiService.enrichArticle(any(Article.class))).thenReturn(CompletableFuture.completedFuture(article));
        when(clusteringService.processClustering(any(Article.class)))
                .thenReturn(CompletableFuture.completedFuture(article));

        // When
        ArticleResponse response = articleService.createArticle(articleRequest);

        // Then
        assertThat(response).isNotNull();
        verify(articleRepository, atLeastOnce()).save(any(Article.class));
        verify(crossReferenceService).processCorrelations(any(Article.class));
        verify(clusteringService).processClustering(any(Article.class));
        verify(aiService).enrichArticle(any(Article.class));
    }

    @Test
    @DisplayName("Get Article By Id - Found -> Return Response")
    void getArticleById_Found_ReturnsResponse() {
        // Given
        when(articleRepository.findById("art1")).thenReturn(Optional.of(article));

        // When
        ArticleResponse response = articleService.getArticleById("art1");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo("art1");
        // Verify view count increment
        assertThat(article.getNombreVues()).isEqualTo(1);
        verify(articleRepository).save(article);
    }

    @Test
    @DisplayName("Get Article By Id - Not Found -> Throw Exception")
    void getArticleById_NotFound_ThrowsException() {
        // Given
        when(articleRepository.findById("unknown")).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> articleService.getArticleById("unknown"))
                .isInstanceOf(ArticleNotFoundException.class);
    }

    @Test
    @DisplayName("Search Articles - By Keyword -> Should Return Page")
    void searchArticles_ByKeyword_ReturnsPage() {
        // Given
        ArticleSearchCriteria criteria = new ArticleSearchCriteria("Test", null, null, null, null, null, null, null, 0,
                10, null, null);
        Page<Article> page = new PageImpl<>(List.of(article));
        when(articleRepository.findByTitreContainingIgnoreCaseOrContenuContainingIgnoreCase(
                eq("Test"), eq("Test"), any(Pageable.class))).thenReturn(page);

        // When
        var result = articleService.searchArticles(criteria);

        // Then
        assertThat(result.content()).hasSize(1);
    }

    @Test
    @DisplayName("Search Articles - By Category Only -> Returns Page")
    void searchArticles_ByCategory_ReturnsPage() {
        // Given
        ArticleSearchCriteria criteria = new ArticleSearchCriteria(null, "cat1", null, null, null, null, null, null, 0,
                10, null, null);
        Page<Article> page = new PageImpl<>(List.of(article));
        when(articleRepository.findByCategorieId(eq("cat1"), any(Pageable.class))).thenReturn(page);

        // When
        var result = articleService.searchArticles(criteria);

        // Then
        assertThat(result.content()).hasSize(1);
    }
}
