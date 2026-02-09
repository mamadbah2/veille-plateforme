package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.Article;

import java.util.concurrent.CompletableFuture;

/**
 * Interface pour les services d'Intelligence Artificielle.
 */
public interface AIService {

    /**
     * Analyse et catégorise un article.
     * Détermine la catégorie, les tags et le niveau de gravité.
     *
     * @param article L'article à analyser.
     * @return Une version enrichie de l'article (ou un objet résultat).
     */
    CompletableFuture<Article> enrichArticle(Article article);

    /**
     * Vérifie si le service IA est disponible.
     * 
     * @return true si disponible.
     */
    boolean isAvailable();
}
