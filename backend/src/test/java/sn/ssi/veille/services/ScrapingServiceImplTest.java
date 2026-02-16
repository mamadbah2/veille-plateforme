package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.web.reactive.function.client.WebClient;

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.services.implementation.ScrapingServiceImpl;

import sn.ssi.veille.services.implementation.ScrapingServiceImpl.HackerNewsItem;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@ExtendWith(MockitoExtension.class)
class ScrapingServiceImplTest {

    @Mock
    private SourceRepository sourceRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private AIService aiService;
    @Mock
    private CrossReferenceService crossReferenceService;
    @Mock
    private CategorieRepository categorieRepository;
    @Mock
    private ClusteringService clusteringService;
    @Mock
    private ContentExtractionService contentExtractionService;

    @InjectMocks
    private ScrapingServiceImpl scrapingService;

    private Article article;
    private Source source;

    @BeforeEach
    void setUp() {
        // Setup WebClient Builder mock
        when(webClientBuilder.build()).thenReturn(webClient);

        // We have to re-inject mocks because the constructor is called before fields
        // are injected?
        // No, @InjectMocks handles constructor injection if possible, but here we have
        // a complex constructor.
        // Let's manually construct to be safe or rely on @InjectMocks if it works with
        // Builder.
        // Since @InjectMocks tries to find the best constructor, it might fail if
        // parameters don't match exactly.
        // Let's manually instantiate to ensure control.
        scrapingService = new ScrapingServiceImpl(
                sourceRepository,
                articleRepository,
                webClientBuilder,
                aiService,
                crossReferenceService,
                categorieRepository,
                clusteringService,
                contentExtractionService);

        article = Article.builder()
                .titre("Test Article")
                .contenu("Content")
                .urlOrigine("http://test.com/1")
                .datePublication(LocalDateTime.now())
                .build();

        source = Source.builder()
                .id("source1")
                .nomSource("Test Source")
                .build();
    }

    @Test
    @DisplayName("Categorize Article - AI Available -> Returns Category Name")
    void categorizeArticle_AIAvailable_ReturnsCategoryName() {
        // Given
        Article enriched = Article.builder().categorieId("cat1").build();
        Categorie cat = Categorie.builder().nomCategorie("Security").build();

        when(aiService.isAvailable()).thenReturn(true);
        when(aiService.enrichArticle(any(Article.class))).thenReturn(CompletableFuture.completedFuture(enriched));
        when(categorieRepository.findById("cat1")).thenReturn(Optional.of(cat));

        // When
        String result = scrapingService.categorizeArticle(article);

        // Then
        assertThat(result).isEqualTo("Security");
    }

    @Test
    @DisplayName("Categorize Article - AI Unavailable -> Returns Default Message")
    void categorizeArticle_AIUnavailable_ReturnsDefault() {
        // Given
        when(aiService.isAvailable()).thenReturn(false);

        // When
        String result = scrapingService.categorizeArticle(article);

        // Then
        assertThat(result).contains("Non catégorisé");
    }

    @Test
    @DisplayName("Determine Gravity - Critical Keyword -> Returns CRITIQUE")
    void determineGravity_CriticalKeyword_ReturnsCritique() {
        // Given
        Article criticalArticle = Article.builder()
                .titre("Zero-day vulnerability found")
                .contenu("Critical issue")
                .build();

        // When
        Gravite g = scrapingService.determineGravity(criticalArticle);

        // Then
        assertThat(g).isEqualTo(Gravite.CRITIQUE);
    }

    @Test
    @DisplayName("Scrape Via API - Hacker News -> Returns Articles")
    void scrapeViaApi_HackerNews_ReturnsArticles() {
        // Given
        Source hnSource = Source.builder()
                .id("hn-source")
                .nomSource("Hacker News")
                .url("https://hacker-news.firebaseio.com")
                .categorieParDefaut("cat1")
                .methodeCollecte(sn.ssi.veille.models.entities.MethodeCollecte.API)
                .maxArticlesPerSync(10)
                .build();

        // Mocks for WebClient chain
        RequestHeadersUriSpec uriSpec = mock(RequestHeadersUriSpec.class);
        RequestHeadersSpec headersSpec = mock(RequestHeadersSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        // 1. Get Top Stories IDs
        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("https://hacker-news.firebaseio.com/v0/topstories.json")).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Integer[].class)).thenReturn(Mono.just(new Integer[] { 12345 }));

        // 2. Get Item Details
        when(uriSpec.uri("https://hacker-news.firebaseio.com/v0/item/12345.json")).thenReturn(headersSpec);
        when(responseSpec.bodyToMono(HackerNewsItem.class)).thenReturn(Mono.just(
                new HackerNewsItem(12345, "HN Title", "http://hn.com/1", 100, 50)));

        // Mock Repository Checks
        when(articleRepository.existsByUrlOrigine("http://hn.com/1")).thenReturn(false);
        when(articleRepository.save(any(Article.class))).thenAnswer(i -> i.getArgument(0));

        // When
        List<Article> result = scrapingService.scrapeViaApi(hnSource);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("HN Title");
        verify(articleRepository).save(any(Article.class));
    }
}
