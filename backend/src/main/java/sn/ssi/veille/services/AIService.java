package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.Article;

import java.util.List;
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

    /**
     * Génère un vecteur d'embedding pour le texte donné.
     *
     * @param text Le texte à vectoriser.
     * @return Une liste de doubles représentant le vecteur.
     */
    CompletableFuture<java.util.List<Double>> getEmbeddings(String text);

    /**
     * Nettoie et reformate un texte brut en utilisant l'IA.
     * Corrige la grammaire, supprime le bruit, et structure en paragraphes.
     *
     * @param rawText Le texte brut issu du scraping.
     * @return Le texte propre et formaté.
     */
    CompletableFuture<String> cleanContent(String rawText);

    /**
     * Génère un résumé à la demande (lazy-load).
     * Ne fonctionne que si le contenu de l'article est suffisant.
     *
     * @param contenu Le contenu complet de l'article.
     * @return Le résumé généré (3-5 phrases).
     */
    CompletableFuture<String> generateSummary(String contenu);

    /**
     * Génère une synthèse (Titre + Résumé) à partir d'une liste d'articles.
     */
    CompletableFuture<String> generateStorySynthesis(List<Article> articles);
}
