package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.Article;
import java.util.concurrent.CompletableFuture;

/**
 * Service gérant le clustering et le regroupement d'articles similaires
 * (Stories).
 */
public interface ClusteringService {

    /**
     * Traite un article pour lui assigner un Cluster (Story ID).
     * 
     * @param article L'article à traiter.
     * @return L'article mis à jour (avec Embedding et StoryId).
     */
    CompletableFuture<Article> processClustering(Article article);
}
