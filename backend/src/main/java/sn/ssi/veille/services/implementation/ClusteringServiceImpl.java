package sn.ssi.veille.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.services.AIService;
import sn.ssi.veille.services.ClusteringService;
import sn.ssi.veille.services.StoryService;
import sn.ssi.veille.utils.VectorUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClusteringServiceImpl implements ClusteringService {

    private final ArticleRepository articleRepository;
    private final StoryService storyService;
    private final AIService aiService;

    // Seuil de similarité (0.85 = très proche sémantiquement)
    private static final double SIMILARITY_THRESHOLD = 0.85;

    @Override
    public CompletableFuture<Article> processClustering(Article article) {
        // On vectorise Titre + Résumé pour plus de précision
        String contentToEmbed = (article.getTitre() + " " + (article.getResume() != null ? article.getResume() : ""))
                .trim();

        if (contentToEmbed.isEmpty()) {
            return CompletableFuture.completedFuture(article);
        }

        return aiService.getEmbeddings(contentToEmbed).thenApply(vector -> {
            if (vector.isEmpty()) {
                log.warn("⚠️ Embedding vide/Offline pour : '{}'. Création Story unique par défaut.",
                        article.getTitre());
                // Fallback: On crée une Story même sans IA pour que l'article soit visible
                articleRepository.save(article);
                storyService.createStoryFromCluster(List.of(article));
                return article;
            }

            article.setVector(vector);

            // Chercher les articles récents (24h) pour comparaison
            LocalDateTime limit = LocalDateTime.now().minusHours(24);
            List<Article> candidates = articleRepository.findByDatePublicationAfter(limit);

            Article bestMatch = null;
            double bestScore = -1.0;

            for (Article candidate : candidates) {
                // Skip self (si déjà en base avec ID) et ceux sans vecteur
                if (candidate.getId() != null && candidate.getId().equals(article.getId()))
                    continue;
                if (candidate.getVector() == null || candidate.getVector().isEmpty())
                    continue;

                double score = VectorUtils.cosineSimilarity(article.getVector(), candidate.getVector());
                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = candidate;
                }
            }

            if (bestMatch != null && bestScore >= SIMILARITY_THRESHOLD) {
                // On rejoint un cluster existant
                if (bestMatch.getStory() == null) {
                    // Le candidat n'a pas encore de Story, on en crée une avec les deux articles
                    // NOTE: Il faut sauvegarder 'article' avant de l'ajouter à une story pour avoir
                    // un ID
                    articleRepository.save(article);

                    log.info("🆕 CREATING STORY: '{}' & '{}' (Score: {})", article.getTitre(), bestMatch.getTitre(),
                            String.format("%.2f", bestScore));

                    storyService.createStoryFromCluster(List.of(bestMatch, article));
                } else {
                    // On rejoint la Story existante
                    // Check if story is present (DBRef might load it lazily or eagerly)
                    if (bestMatch.getStory() != null) {
                        String storyId = bestMatch.getStory().getId();
                        log.info("🔗 JOINED STORY: '{}' -> Story {} (Score: {})", article.getTitre(),
                                storyId, String.format("%.2f", bestScore));

                        // On sauvegarde d'abord l'article pour qu'il ait un ID
                        articleRepository.save(article);
                        storyService.addArticleToStory(storyId, article);
                    } else {
                        log.warn("Article {} has match but Story is null, treating as new cluster potential",
                                bestMatch.getTitre());
                        // Fallback logic if needed, or just save
                        articleRepository.save(article);
                    }
                }
            } else {
                // Pas de cluster trouvé -> On crée une NOUVELLE Story pour cet article unique
                log.info("📢 No match found for '{}'. Creating new Single-Source Story.", article.getTitre());
                articleRepository.save(article);
                storyService.createStoryFromCluster(List.of(article));
            }

            return article;
        });
    }
}
