package sn.ssi.veille.services;

import java.util.concurrent.CompletableFuture;

/**
 * Service pour extraire le contenu complet d'un article à partir de son URL.
 */
public interface ContentExtractionService {

    /**
     * Extrait le contenu textuel principal d'une page Web.
     * Tente de supprimer les publicités, menus et autres bruits.
     *
     * @param url L'URL de l'article.
     * @return Un CompletableFuture contenant le texte extrait.
     */
    CompletableFuture<String> extractFullContent(String url);
}
