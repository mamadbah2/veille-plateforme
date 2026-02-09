package sn.ssi.veille.services.implementation;

import org.springframework.stereotype.Service;
import sn.ssi.veille.exceptions.SourceAlreadyExistsException;
import sn.ssi.veille.exceptions.SourceNotFoundException;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.models.entities.Source.SourceType;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.services.SourceService;
import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

/**
 * Implémentation du service Source.
 * Gère les opérations CRUD pour les sources de veille.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @Override
    public SourceResponse createSource(SourceRequest request) {
        // Vérifier si une source avec cette URL existe déjà
        if (sourceRepository.findByUrl(request.url()).isPresent()) {
            throw new SourceAlreadyExistsException("Une source avec cette URL existe déjà: " + request.url());
        }

        Source source = Source.builder()
                .url(request.url())
                .nomSource(request.nomSource())
                .description(request.description())
                .logoUrl(request.logoUrl())
                .methodeCollecte(request.methodeCollecte())
                .active(request.active() != null ? request.active() : true)
                .frequenceScraping(request.frequenceScraping() != null ? request.frequenceScraping() : 60)
                .apiKey(request.apiKey())
                .headers(request.headers())
                .langue(request.langue())
                .priorite(request.priorite() != null ? request.priorite() : 5)
                .categorieParDefaut(request.categorieParDefaut())
                .timeout(request.timeout() != null ? request.timeout() : 30)
                .retryCount(request.retryCount() != null ? request.retryCount() : 3)
                .rateLimitPerMinute(request.rateLimitPerMinute() != null ? request.rateLimitPerMinute() : 60)
                .maxArticlesPerSync(request.maxArticlesPerSync() != null ? request.maxArticlesPerSync() : 100)
                .trustScore(request.trustScore() != null ? request.trustScore() : 5)
                .verified(request.verified() != null ? request.verified() : false)
                .sourceType(request.sourceType() != null ? request.sourceType() : SourceType.UNKNOWN)
                .selectorTitle(request.selectorTitle())
                .selectorContent(request.selectorContent())
                .selectorDate(request.selectorDate())
                .build();

        Source saved = sourceRepository.save(source);
        return toResponse(saved);
    }

    @Override
    public SourceResponse getSourceById(String id) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source non trouvée avec l'id: " + id));
        return toResponse(source);
    }

    @Override
    public List<SourceResponse> getAllSources() {
        return sourceRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Récupère toutes les sources actives.
     */
    public List<SourceResponse> getActiveSources() {
        return sourceRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Met à jour une source existante.
     */
    public SourceResponse updateSource(String id, SourceRequest request) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source non trouvée avec l'id: " + id));

        // Identité
        if (request.url() != null)
            source.setUrl(request.url());
        if (request.nomSource() != null)
            source.setNomSource(request.nomSource());
        if (request.description() != null)
            source.setDescription(request.description());
        if (request.logoUrl() != null)
            source.setLogoUrl(request.logoUrl());

        // Collecte
        if (request.methodeCollecte() != null)
            source.setMethodeCollecte(request.methodeCollecte());
        if (request.active() != null)
            source.setActive(request.active());
        if (request.frequenceScraping() != null)
            source.setFrequenceScraping(request.frequenceScraping());

        // Auth
        if (request.apiKey() != null)
            source.setApiKey(request.apiKey());
        if (request.headers() != null)
            source.setHeaders(request.headers());

        // Config
        if (request.langue() != null)
            source.setLangue(request.langue());
        if (request.priorite() != null)
            source.setPriorite(request.priorite());
        if (request.categorieParDefaut() != null)
            source.setCategorieParDefaut(request.categorieParDefaut());
        if (request.timeout() != null)
            source.setTimeout(request.timeout());
        if (request.retryCount() != null)
            source.setRetryCount(request.retryCount());
        if (request.rateLimitPerMinute() != null)
            source.setRateLimitPerMinute(request.rateLimitPerMinute());
        if (request.maxArticlesPerSync() != null)
            source.setMaxArticlesPerSync(request.maxArticlesPerSync());

        // Crédibilité
        if (request.trustScore() != null)
            source.setTrustScore(request.trustScore());
        if (request.verified() != null)
            source.setVerified(request.verified());
        if (request.sourceType() != null)
            source.setSourceType(request.sourceType());

        // Scraping
        if (request.selectorTitle() != null)
            source.setSelectorTitle(request.selectorTitle());
        if (request.selectorContent() != null)
            source.setSelectorContent(request.selectorContent());
        if (request.selectorDate() != null)
            source.setSelectorDate(request.selectorDate());

        Source updated = sourceRepository.save(source);
        return toResponse(updated);
    }

    /**
     * Active une source.
     */
    public SourceResponse activateSource(String id) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source non trouvée avec l'id: " + id));
        source.setActive(true);
        Source updated = sourceRepository.save(source);
        return toResponse(updated);
    }

    /**
     * Désactive une source.
     */
    public SourceResponse deactivateSource(String id) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source non trouvée avec l'id: " + id));
        source.setActive(false);
        Source updated = sourceRepository.save(source);
        return toResponse(updated);
    }

    /**
     * Supprime une source.
     */
    public void deleteSource(String id) {
        if (!sourceRepository.existsById(id)) {
            throw new SourceNotFoundException("Source non trouvée avec l'id: " + id);
        }
        sourceRepository.deleteById(id);
    }

    /**
     * Convertit une entité Source en SourceResponse.
     */
    private SourceResponse toResponse(Source source) {
        return new SourceResponse(
                source.getId(),
                source.getUrl(),
                source.getNomSource(),
                source.getDescription(),
                source.getLogoUrl(),
                source.getMethodeCollecte(),
                source.isActive(),
                source.getFrequenceScraping(),
                source.getDerniereSyncro(),
                source.getNextSyncAt(),
                source.getLangue(),
                source.getPriorite(),
                source.getCategorieParDefaut(),
                source.getTrustScore(),
                source.isVerified(),
                source.getSourceType(),
                source.getTotalArticlesCollected(),
                source.getArticlesLastSync(),
                source.getLastError(),
                source.getLastErrorAt(),
                source.getConsecutiveFailures(),
                source.getCreatedAt());
    }
}
