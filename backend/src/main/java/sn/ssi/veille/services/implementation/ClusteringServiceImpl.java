package sn.ssi.veille.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.services.AIService;
import sn.ssi.veille.services.ClusteringService;
import sn.ssi.veille.utils.VectorUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClusteringServiceImpl implements ClusteringService {

    private final ArticleRepository articleRepository;
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
                log.warn("Embedding vide pour : {}", article.getTitre());
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
                if (bestMatch.getStoryId() == null) {
                    // Le candidat n'a pas encore de Story, on en crée une
                    String newStoryId = UUID.randomUUID().toString();
                    bestMatch.setStoryId(newStoryId);
                    articleRepository.save(bestMatch); // Update immédiat du candidat
                    article.setStoryId(newStoryId);
                    log.info("🆕 STORY CREATED: '{}' & '{}' (Score: {})", article.getTitre(), bestMatch.getTitre(),
                            String.format("%.2f", bestScore));
                } else {
                    // On rejoint la Story existante
                    article.setStoryId(bestMatch.getStoryId());
                    log.info("🔗 JOINED STORY: '{}' -> Cluster {} (Score: {})", article.getTitre(),
                            bestMatch.getStoryId(), String.format("%.2f", bestScore));
                }
            } else {
                // Pas de cluster trouvé
                // On laisse storyId à null pour indiquer "Article unique" (ou on pourrait créer
                // un ID unique)
                log.debug("No cluster found for '{}' (Best: {})", article.getTitre(), String.format("%.2f", bestScore));
            }

            // Sauvegarde de l'article avec son vecteur et son StoryId
            return articleRepository.save(article);
        });
    }
}
