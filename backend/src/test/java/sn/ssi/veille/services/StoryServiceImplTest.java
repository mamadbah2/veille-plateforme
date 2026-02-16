package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.entities.Story;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.StoryRepository;
import sn.ssi.veille.services.implementation.StoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class StoryServiceImplTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CategorieRepository categorieRepository;

    @InjectMocks
    private StoryServiceImpl storyService;

    @Test
    @DisplayName("Create Story From Cluster - Valid Articles -> Returns Story")
    void createStoryFromCluster_ValidArticles_ReturnsStory() {
        // Given
        Article a1 = Article.builder().id("art1").titre("Article 1").categorieId("cat1").build();
        Article a2 = Article.builder().id("art2").titre("Article 2").categorieId("cat2").build();
        List<Article> cluster = List.of(a1, a2);

        Categorie c1 = new Categorie();
        c1.setNomCategorie("Cybersecurity");
        Categorie c2 = new Categorie();
        c2.setNomCategorie("AI");

        when(categorieRepository.findById("cat1")).thenReturn(Optional.of(c1));
        when(categorieRepository.findById("cat2")).thenReturn(Optional.of(c2));

        // Mock save to return object with ID
        when(storyRepository.save(any(Story.class))).thenAnswer(invocation -> {
            Story s = invocation.getArgument(0);
            s.setId("story1");
            return s;
        });

        // When
        Story result = storyService.createStoryFromCluster(cluster);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("story1");
        assertThat(result.getArticles()).hasSize(2);
        // Should contain category names
        assertThat(result.getCategories()).contains("Cybersecurity", "AI");
        // Articles should be updated
        verify(articleRepository, times(2)).save(any(Article.class));
    }

    @Test
    @DisplayName("Add Article To Story - Valid -> Updates Story & Article")
    void addArticleToStory_Valid_UpdatesStoryAndArticle() {
        // Given
        Story story = new Story();
        story.setId("story1");
        story.setArticles(new ArrayList<>());
        story.setCategories(new HashSet<>());

        Article article = Article.builder().id("art3").titre("New Article").categorieId("cat1").build();
        Categorie cat = new Categorie();
        cat.setNomCategorie("Cybersecurity");

        when(storyRepository.findById("story1")).thenReturn(Optional.of(story));
        when(categorieRepository.findById("cat1")).thenReturn(Optional.of(cat));

        // When
        storyService.addArticleToStory("story1", article);

        // Then
        verify(storyRepository, times(2)).save(story);
        verify(articleRepository).save(article);
        assertThat(story.getArticles()).contains(article);
        assertThat(story.getCategories()).contains("Cybersecurity");
    }

    @Test
    @DisplayName("Get Trending Stories -> Returns Sorted List")
    void getTrendingStories_ReturnsSortedList() {
        // Given
        List<Story> stories = List.of(new Story(), new Story());
        when(storyRepository.findByDateMiseAJourAfterOrderByDateMiseAJourDesc(any(LocalDateTime.class)))
                .thenReturn(stories);

        // When
        List<Story> result = storyService.getTrendingStories();

        // Then
        assertThat(result).hasSize(2);
    }
}
