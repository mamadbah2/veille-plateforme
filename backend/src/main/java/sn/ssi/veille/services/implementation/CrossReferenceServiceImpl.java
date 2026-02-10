package sn.ssi.veille.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.repositories.ArticleRepository;
import sn.ssi.veille.services.CrossReferenceService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrossReferenceServiceImpl implements CrossReferenceService {

    private final ArticleRepository articleRepository;

    @Override
    public void processCorrelations(Article target) {
        if (target.getTags() == null || target.getTags().length == 0) {
            return;
        }

        // Limit search to last 30 days for performance
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        List<Article> candidates = articleRepository.findByDatePublicationAfter(since);

        // Normalized tags of target
        Set<String> targetTags = Arrays.stream(target.getTags())
                .map(String::toLowerCase)
                .map(String::trim)
                .collect(Collectors.toSet());

        int linksFound = 0;

        for (Article candidate : candidates) {
            // Skip self or invalid ID
            if (candidate.getId() == null || candidate.getId().equals(target.getId()))
                continue;

            // Skip already linked (optimization)
            if (target.getRelatedArticleIds() != null && target.getRelatedArticleIds().contains(candidate.getId()))
                continue;

            boolean linked = false;

            // Strategy 1: Common Tags (> 2 tags in common) - Threshold set to 3
            if (candidate.getTags() != null) {
                Set<String> candidateTags = Arrays.stream(candidate.getTags())
                        .map(String::toLowerCase)
                        .map(String::trim)
                        .collect(Collectors.toSet());

                // Calculate intersection
                Set<String> intersection = new HashSet<>(targetTags);
                intersection.retainAll(candidateTags);

                // If specialized tags match (e.g. not just "Security" but "Ransomware",
                // "Cisco")
                if (intersection.size() >= 3) {
                    linked = true;
                    log.info("🔗 Correlation found (Tags): [{}] <-> [{}] (Shared: {})",
                            target.getTitre(), candidate.getTitre(), intersection);
                }
            }

            if (linked) {
                // Bidirectional Link
                addLink(target, candidate);
                addLink(candidate, target);
                articleRepository.save(candidate); // Save the neighbor immediately
                linksFound++;
            }
        }

        if (linksFound > 0) {
            articleRepository.save(target); // Save target with new links
            log.info("✅ Article '{}' linked to {} other articles.", target.getTitre(), linksFound);
        }
    }

    private void addLink(Article source, Article destination) {
        if (source.getRelatedArticleIds() == null) {
            source.setRelatedArticleIds(new ArrayList<>());
        }
        if (!source.getRelatedArticleIds().contains(destination.getId())) {
            source.getRelatedArticleIds().add(destination.getId());
        }
    }
}
