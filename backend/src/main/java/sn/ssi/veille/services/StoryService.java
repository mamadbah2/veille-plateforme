package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Story;

import java.util.List;

public interface StoryService {

    /**
     * Crée une nouvelle Story à partir d'un groupe d'articles (Cluster).
     * Initialise la story en mode DRAFT.
     */
    Story createStoryFromCluster(List<Article> articles);

    /**
     * Ajoute un article à une Story existante et demande une mise à jour de la
     * synthèse.
     */
    void addArticleToStory(String storyId, Article article);

    /**
     * Récupère les stories "Trending" (récentes et peuplées).
     */
    List<Story> getTrendingStories();

    /**
     * Récupère une story par son ID.
     */
    Story getStoryById(String id);
}
