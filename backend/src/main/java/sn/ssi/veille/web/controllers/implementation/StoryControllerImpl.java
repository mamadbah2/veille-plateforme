package sn.ssi.veille.web.controllers.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Story;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.models.repositories.StoryRepository; // Import Added
import sn.ssi.veille.services.StoryService;
import sn.ssi.veille.web.controllers.StoryController;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;
import sn.ssi.veille.web.dto.responses.StoryResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stories")
@RequiredArgsConstructor
public class StoryControllerImpl implements StoryController {

    private final StoryService storyService;
    private final CategorieRepository categorieRepository;
    private final SourceRepository sourceRepository;
    private final StoryRepository storyRepository; // Field Added

    @Override
    @GetMapping("/trending")
    public ResponseEntity<List<StoryResponse>> getTrendingStories() {
        List<Story> stories = storyService.getTrendingStories();
        List<StoryResponse> response = stories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<StoryResponse> getStoryById(@PathVariable String id) {
        Story story = storyService.getStoryById(id);
        if (story == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toResponse(story));
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<StoryResponse>> getStoriesByCategory(@PathVariable String categoryName) {
        List<Story> stories = storyRepository.findByCategoriesContaining(categoryName);
        List<StoryResponse> response = stories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private StoryResponse toResponse(Story story) {
        // [V2 Logic] Select Main Article (Champion) - e.g., longest content
        Article mainArticle = story.getArticles().stream()
                .reduce((a1, a2) -> {
                    String c1 = a1.getContenu() != null ? a1.getContenu() : "";
                    String c2 = a2.getContenu() != null ? a2.getContenu() : "";
                    return c1.length() > c2.length() ? a1 : a2;
                })
                .orElse(null);

        String mainContent = (mainArticle != null && mainArticle.getContenu() != null) ? mainArticle.getContenu() : "";

        // If title is missing (Draft), use Main Article title
        String titre = (story.getTitre() != null && !story.getTitre().isBlank()) ? story.getTitre()
                : (mainArticle != null ? mainArticle.getTitre() : "Sans titre");

        // Mockup Fields
        String mainSourceName = "Unknown";
        String mainImageUrl = null;
        List<String> mainTags = List.of();
        int readingTime = 0;
        String mainSubtitle = null;
        String mainAuthorName = null;
        String sourceLogoUrl = null;

        if (mainArticle != null) {
            if (mainArticle.getSourceId() != null) {
                var sourceOpt = sourceRepository.findById(mainArticle.getSourceId());
                if (sourceOpt.isPresent()) {
                    mainSourceName = sourceOpt.get().getNomSource();
                    sourceLogoUrl = sourceOpt.get().getLogoUrl();
                }
            }
            mainImageUrl = mainArticle.getImageUrl();
            mainTags = (mainArticle.getTags() != null) ? java.util.Arrays.asList(mainArticle.getTags()) : List.of();

            // Subtitle = Article Resume (Scraped teaser)
            mainSubtitle = mainArticle.getResume();

            // Author
            mainAuthorName = mainArticle.getAuteur();

            // Calc Reading Time (approx 200 words/min)
            if (mainContent != null && !mainContent.isBlank()) {
                int wordCount = mainContent.split("\\s+").length;
                readingTime = (int) Math.ceil(wordCount / 200.0);
                if (readingTime < 1)
                    readingTime = 1;
            }
        }

        List<ArticleSummaryResponse> relatedArticles = story.getArticles().stream()
                .map(this::toArticleResponse)
                .collect(Collectors.toList());

        return new StoryResponse(
                story.getId(),
                titre,
                mainContent,
                mainSubtitle, // New Field
                mainSourceName,
                mainAuthorName, // New Field
                mainImageUrl,
                sourceLogoUrl, // New Field
                mainTags,
                readingTime,
                story.getResume(), // Ce champ restera null tant que l'IA n'est pas appelée
                story.getDateMiseAJour(),
                story.getCategories(),
                relatedArticles);
    }

    private ArticleSummaryResponse toArticleResponse(Article article) {
        String nomSource = "Unknown";
        if (article.getSourceId() != null) {
            nomSource = sourceRepository.findById(article.getSourceId())
                    .map(s -> s.getNomSource()) // Correct getter
                    .orElse("Unknown");
        }

        String nomCategorie = "Uncategorized";
        String couleurCategorie = "#808080"; // Grey default
        if (article.getCategorieId() != null) {
            var catOpt = categorieRepository.findById(article.getCategorieId());
            if (catOpt.isPresent()) {
                nomCategorie = catOpt.get().getNomCategorie(); // Correct getter
                couleurCategorie = catOpt.get().getCouleur();
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
}
