package sn.ssi.veille.web.controllers.implementation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Endpoints pour la recherche globale (Spotlight)")
@Slf4j
public class SpotlightSearchController {

    private final ArticleRepository articleRepository;
    private final CategorieRepository categorieRepository;

    @Operation(summary = "Recherche Spotlight (Articles, Catégories, Tags)")
    @GetMapping("/spotlight")
    public ResponseEntity<Map<String, Object>> spotlightSearch(@RequestParam(required = false) String query) {

        // 2. Recherche Catégories (Max 3)
        List<Categorie> foundCategories = List.of();
        if (query != null && !query.isBlank()) {
            foundCategories = categorieRepository.findByNomCategorieContainingIgnoreCase(query);
        }

        List<CategorieResponse> categories = foundCategories.stream()
                .limit(3)
                .map(this::toCategorieResponse)
                .collect(Collectors.toList());

        // 1. Recherche Articles (Max 5) - "Smart Search" (Titre OR Contenu OR
        // Catégorie)
        List<ArticleSummaryResponse> articles = List.of();
        if (query != null && !query.isBlank()) {
            List<String> catIds = foundCategories.stream().map(Categorie::getId).collect(Collectors.toList());

            articles = articleRepository.findByTitreContainingIgnoreCaseOrContenuContainingIgnoreCaseOrCategorieIdIn(
                    query, query, catIds, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "datePublication")))
                    .stream()
                    .map(this::toArticleResponse)
                    .collect(Collectors.toList());
        }

        // 3. Tags (Tendances OU Recherche)
        List<String> tags;
        if (query != null && !query.isBlank()) {
            // Si recherche active : on cherche les tags correspondants (ex: "Jav" ->
            // "Java", "Javascript")
            tags = articleRepository.findMatchingTags(query, 5)
                    .stream()
                    .map(sn.ssi.veille.models.repositories.ArticleRepository.TagCountResult::tag)
                    .collect(Collectors.toList());
        } else {
            // Sinon : on affiche les tags tendances globaux
            tags = articleRepository.findTrendingTags(java.time.LocalDateTime.now().minusDays(7), 5)
                    .stream()
                    .map(sn.ssi.veille.models.repositories.ArticleRepository.TagCountResult::tag)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(Map.of(
                "articles", articles,
                "categories", categories,
                "trendingTags", tags));
    }

    // Mappers simplifiés (Dupliqués pour éviter dépendance circulaire, idéalement
    // dans un Mapper)
    private ArticleSummaryResponse toArticleResponse(Article article) {
        String imageUrl = null; // Basic mapper
        return new ArticleSummaryResponse(
                article.getId(),
                article.getTitre(),
                article.getResume(),
                imageUrl,
                article.getGravite(),
                "Unknown Source",
                "Unknown Category",
                "#000000",
                0L,
                article.getDatePublication());
    }

    private CategorieResponse toCategorieResponse(Categorie categorie) {
        long count = 0; // Pas de count ici pour perf
        return new CategorieResponse(
                categorie.getId(),
                categorie.getNomCategorie(),
                categorie.getDescription(),
                categorie.getCouleur(),
                categorie.getIcone(),
                categorie.getImageUrl(),
                count,
                categorie.getCreatedAt());
    }
}
