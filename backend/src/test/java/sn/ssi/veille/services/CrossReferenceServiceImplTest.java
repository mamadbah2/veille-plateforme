package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.services.implementation.CrossReferenceServiceImpl;

@ExtendWith(MockitoExtension.class)
class CrossReferenceServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private CrossReferenceServiceImpl crossReferenceService;

    @Test
    @DisplayName("Process Correlations - Matching Tags -> Links Articles")
    void processCorrelations_MatchingTags_LinksArticles() {
        // Given
        Article target = Article.builder()
                .id("target1")
                .titre("Target Article")
                .tags(new String[] { "security", "cve", "ransomware" })
                .build();

        Article candidate = Article.builder()
                .id("cand1")
                .titre("Candidate Article")
                .tags(new String[] { "security", "cve", "ransomware", "patch" })
                .build();

        // Mock repository search
        when(articleRepository.findByDatePublicationAfter(any(LocalDateTime.class)))
                .thenReturn(List.of(candidate));

        // When
        crossReferenceService.processCorrelations(target);

        // Then
        // Should save both articles with updated links
        verify(articleRepository).save(candidate);
        verify(articleRepository).save(target);

        assertThat(target.getRelatedArticleIds()).contains("cand1");
        assertThat(candidate.getRelatedArticleIds()).contains("target1");
    }

    @Test
    @DisplayName("Process Correlations - No Matching Tags -> No Link")
    void processCorrelations_NoMatchingTags_NoLink() {
        // Given
        Article target = Article.builder()
                .id("target1")
                .tags(new String[] { "java", "spring" })
                .build();

        Article candidate = Article.builder()
                .id("cand1")
                .tags(new String[] { "python", "django" })
                .build();

        when(articleRepository.findByDatePublicationAfter(any(LocalDateTime.class)))
                .thenReturn(List.of(candidate));

        // When
        crossReferenceService.processCorrelations(target);

        // Then
        verify(articleRepository, never()).save(any(Article.class));
        assertThat(target.getRelatedArticleIds()).isNullOrEmpty();
    }
}
