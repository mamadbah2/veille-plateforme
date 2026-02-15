package sn.ssi.veille.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Story;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.StoryRepository;
import sn.ssi.veille.services.StoryService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final ArticleRepository articleRepository;
    private final CategorieRepository categorieRepository;

    @Override
    public Story createStoryFromCluster(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return null;
        }

        // Création initiale de la Story
        Story story = Story.builder()
                .etat(Story.EtatStory.DRAFT)
                .articles(new java.util.ArrayList<>(articles)) // Copie mutable
                .categories(new HashSet<>())
                .dateCreation(LocalDateTime.now())
                .dateModification(LocalDateTime.now())
                .dateMiseAJour(LocalDateTime.now())
                .build();

        // Fusion des catégories
        for (Article a : articles) {
            if (a.getCategorieId() != null) {
                categorieRepository.findById(a.getCategorieId())
                        .ifPresent(c -> story.getCategories().add(c.getNomCategorie()));
            }
        }

        Story savedStory = storyRepository.save(story);

        // Mise à jour des articles pour pointer vers cette Story
        for (Article a : articles) {
            a.setStory(savedStory);
            articleRepository.save(a);
        }

        // Déclenchement synthèse IA (Async)
        triggerAISynthesis(savedStory);

        log.info("📢 New Story created with {} articles. ID: {}", articles.size(), savedStory.getId());
        return savedStory;
    }

    @Override
    public void addArticleToStory(String storyId, Article article) {
        Story story = storyRepository.findById(storyId).orElse(null);
        if (story == null)
            return;

        // Ajouter l'article à la story
        if (story.getArticles() == null) {
            story.setArticles(new java.util.ArrayList<>(List.of(article)));
        } else {
            story.getArticles().add(article);
        }

        // Mettre à jour les catégories
        if (article.getCategorieId() != null) {
            categorieRepository.findById(article.getCategorieId())
                    .ifPresent(c -> story.getCategories().add(c.getNomCategorie()));
        }

        story.setDateMiseAJour(LocalDateTime.now());
        storyRepository.save(story);

        // Lier l'article
        article.setStory(story);
        articleRepository.save(article);

        // Nouvelle synthèse si nécessaire (debounce possible ici, mais on fait simple)
        triggerAISynthesis(story);
    }

    @Override
    public List<Story> getTrendingStories() {
        // Retourne les stories mises à jour dans les dernières 24h
        return storyRepository.findByDateMiseAJourAfterOrderByDateMiseAJourDesc(LocalDateTime.now().minusHours(24));
    }

    @Override
    public Story getStoryById(String id) {
        return storyRepository.findById(id).orElse(null);
    }

    private void triggerAISynthesis(Story story) {
        if (story.getArticles() == null || story.getArticles().isEmpty()) {
            return;
        }

        // [V2 Logic] On-Demand Mode
        // We do NOT call AI automatically anymore.
        // We just ensure the Story is PUBLISHED so it appears in the frontend.
        // The 'resume' field will be null/empty until the user clicks "AI Summary".

        // Pick a default title if missing
        if (story.getTitre() == null || story.getTitre().isBlank()) {
            Article champion = story.getArticles().stream()
                    .reduce((a1, a2) -> {
                        String c1 = a1.getContenu() != null ? a1.getContenu() : "";
                        String c2 = a2.getContenu() != null ? a2.getContenu() : "";
                        return c1.length() > c2.length() ? a1 : a2;
                    })
                    .orElse(story.getArticles().get(0));
            story.setTitre(champion.getTitre());
        }

        story.setEtat(Story.EtatStory.PUBLISHED);
        story.setDateMiseAJour(LocalDateTime.now());
        storyRepository.save(story);

        log.info("ℹ️ Story (ID: {}) created/updated in On-Demand mode. AI Synthesis skipped.", story.getId());
    }
}
