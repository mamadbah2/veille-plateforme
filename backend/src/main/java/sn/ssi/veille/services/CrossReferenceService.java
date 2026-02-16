package sn.ssi.veille.services;

import sn.ssi.veille.models.entities.Article;

public interface CrossReferenceService {
    /**
     * Analyse un article et trouve des corrélations avec les articles existants.
     * Met à jour les liens de manière bidirectionnelle.
     * 
     * @param article L'article à analyser (doit avoir des tags/catégories).
     */
    void processCorrelations(Article article);
}
