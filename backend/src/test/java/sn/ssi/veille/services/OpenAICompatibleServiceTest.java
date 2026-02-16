package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import sn.ssi.veille.config.AIConfig;
import sn.ssi.veille.config.PromptConfig;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.services.implementation.OpenAICompatibleService;

@ExtendWith(MockitoExtension.class)
class OpenAICompatibleServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private AIConfig aiConfig;
    @Mock
    private PromptConfig promptConfig;
    @Mock
    private CategorieRepository categorieRepository;

    private OpenAICompatibleService aiService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        // Setup WebClient Builder mock chain for constructor
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(anyString(), anyString())).thenReturn(webClientBuilder); // Added for
                                                                                                     // Authorizaton
                                                                                                     // header
        when(webClientBuilder.clientConnector(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.exchangeStrategies(any(ExchangeStrategies.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // Setup AI Config
        lenient().when(aiConfig.getUrl()).thenReturn("http://localhost:1234");
        lenient().when(aiConfig.getTimeout()).thenReturn(1000L);
        lenient().when(aiConfig.getModel()).thenReturn("test-model");
        lenient().when(aiConfig.getApiKey()).thenReturn("test-key");

        // Mock Prompts
        PromptConfig.Enrichment enrichment = new PromptConfig.Enrichment();
        enrichment.setSystem("Test System");
        lenient().when(promptConfig.getEnrichment()).thenReturn(enrichment);

        PromptConfig.Cleaning cleaning = new PromptConfig.Cleaning();
        cleaning.setSystem("Test System");
        lenient().when(promptConfig.getCleaning()).thenReturn(cleaning);

        PromptConfig.Summary summary = new PromptConfig.Summary();
        summary.setSystem("Test System");
        lenient().when(promptConfig.getSummary()).thenReturn(summary);

        PromptConfig.Clustering clustering = new PromptConfig.Clustering();
        clustering.setSystem("Test System");
        lenient().when(promptConfig.getClustering()).thenReturn(clustering);

        aiService = new OpenAICompatibleService(webClientBuilder, aiConfig, promptConfig, categorieRepository);
    }

    @Test
    @DisplayName("IsAvailable - Service Up -> Should Return True")
    @SuppressWarnings("unchecked")
    void isAvailable_Success() {
        // Given
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/v1/models")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.just(ResponseEntity.ok().build()));

        // When
        boolean result = aiService.isAvailable();

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("IsAvailable - Service Down -> Should Return False")
    @SuppressWarnings("unchecked")
    void isAvailable_Failure() {
        // Given
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/v1/models")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.error(new RuntimeException("Down")));

        // When
        boolean result = aiService.isAvailable();

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("EnrichArticle - AI Offline -> Should Return Original Article")
    @SuppressWarnings("unchecked")
    void enrichArticle_Offline_ShouldReturnOriginal() {
        // Given - Mock availability check to false
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/v1/models")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.error(new RuntimeException("Offline")));

        Article article = Article.builder().titre("Test").build();

        // When
        CompletableFuture<Article> future = aiService.enrichArticle(article);
        Article result = future.join();

        // Then
        assertThat(result).isEqualTo(article);
    }
}
