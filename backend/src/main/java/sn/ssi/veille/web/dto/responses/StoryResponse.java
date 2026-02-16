package sn.ssi.veille.web.dto.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record StoryResponse(
        String id,
        String titre, // Titre de l'article principal
        String mainContent, // Contenu complet (pour le Detail View)
        String mainSubtitle, // Sous-titre ou accroche (Article.resume)
        String mainSourceName, // Nom de la source principale (pour la Liste)
        String mainAuthorName, // Auteur de l'article
        String mainImageUrl, // Image de l'article principal
        String sourceLogoUrl, // Logo de la source (pour l'avatar)
        List<String> mainTags, // Tags de l'article principal
        int readingTime, // Temps de lecture estimé (mn)
        String resume, // Résumé IA (si généré)
        LocalDateTime dateMiseAJour,
        Set<String> categories,
        List<ArticleSummaryResponse> relatedArticles // Les autres articles du cluster
) {
}
