package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Story;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.services.implementation.ClusteringServiceImpl;

@ExtendWith(MockitoExtension.class)
class ClusteringServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private StoryService storyService;

    @Mock
    private AIService aiService;

    @InjectMocks
    private ClusteringServiceImpl clusteringService;

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article();
        article.setId("art1");
        article.setTitre("New AI Regulation");
        article.setResume("EU passes new AI act.");
        article.setDatePublication(LocalDateTime.now());
    }

    @Test
    @DisplayName("Process Clustering - No Embedding Generated -> Should Fallback to Single Story")
    void processClustering_NoEmbedding_ShouldFallback() {
        // Given
        when(aiService.getEmbeddings(anyString())).thenReturn(CompletableFuture.completedFuture(List.of()));

        // When
        Article result = clusteringService.processClustering(article).join();

        // Then
        assertThat(result).isNotNull();
        verify(articleRepository).save(article);
        verify(storyService).createStoryFromCluster(anyList());
    }

    @Test
    @DisplayName("Process Clustering - No Candidates -> Should Create New Story")
    void processClustering_NoCandidates_ShouldCreateNewStory() {
        // Given
        List<Double> vector = List.of(1.0, 0.0, 0.0);
        when(aiService.getEmbeddings(anyString())).thenReturn(CompletableFuture.completedFuture(vector));
        when(articleRepository.findByDatePublicationAfter(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        // When
        Article result = clusteringService.processClustering(article).join();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getVector()).isEqualTo(vector);
        verify(storyService).createStoryFromCluster(anyList());
    }

    @Test
    @DisplayName("Process Clustering - High Similarity Match (No Existing Story) -> Should Create Shared Story")
    void processClustering_MatchFound_NoExistingStory_ShouldCreateSharedStory() {
        // Given
        List<Double> vector1 = List.of(1.0, 0.0);
        List<Double> vector2 = List.of(0.99, 0.01); // Very similar

        when(aiService.getEmbeddings(anyString())).thenReturn(CompletableFuture.completedFuture(vector1));

        Article candidate = new Article();
        candidate.setId("art2");
        candidate.setTitre("AI Act in Europe");
        candidate.setVector(vector2);
        candidate.setStory(null); // No story yet

        when(articleRepository.findByDatePublicationAfter(any(LocalDateTime.class))).thenReturn(List.of(candidate));

        // When
        Article result = clusteringService.processClustering(article).join();

        // Then
        verify(articleRepository).save(article);
        // Verify we created a cluster with BOTH articles
        verify(storyService).createStoryFromCluster(
                argThat(list -> list.size() == 2 && list.contains(article) && list.contains(candidate)));
    }

    @Test
    @DisplayName("Process Clustering - High Similarity Match (Existing Story) -> Should Join Story")
    void processClustering_MatchFound_ExistingStory_ShouldJoinStory() {
        // Given
        List<Double> vector1 = List.of(1.0, 0.0);
        List<Double> vector2 = List.of(0.99, 0.01);

        when(aiService.getEmbeddings(anyString())).thenReturn(CompletableFuture.completedFuture(vector1));

        Article candidate = new Article();
        candidate.setId("art2");
        candidate.setVector(vector2);
        Story existingStory = new Story();
        existingStory.setId("story123");
        candidate.setStory(existingStory);

        when(articleRepository.findByDatePublicationAfter(any(LocalDateTime.class))).thenReturn(List.of(candidate));

        // When
        Article result = clusteringService.processClustering(article).join();

        // Then
        verify(articleRepository).save(article);
        verify(storyService).addArticleToStory(eq("story123"), eq(article));
    }
}
