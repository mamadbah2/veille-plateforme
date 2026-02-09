package sn.ssi.veille.services.implementation;

import org.springframework.stereotype.Service;
import sn.ssi.veille.exceptions.SourceAlreadyExistsException;
import sn.ssi.veille.exceptions.SourceNotFoundException;
import sn.ssi.veille.models.entities.Source;
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
                .methodeCollecte(request.methodeCollecte())
                .active(request.active() != null ? request.active() : true)
                .frequenceScraping(request.frequenceScraping() != null ? request.frequenceScraping() : 60)
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

        if (request.url() != null)
            source.setUrl(request.url());
        if (request.nomSource() != null)
            source.setNomSource(request.nomSource());
        if (request.methodeCollecte() != null)
            source.setMethodeCollecte(request.methodeCollecte());
        if (request.active() != null)
            source.setActive(request.active());
        if (request.frequenceScraping() != null)
            source.setFrequenceScraping(request.frequenceScraping());

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
                source.getMethodeCollecte(),
                source.isActive(),
                source.getFrequenceScraping(),
                source.getDerniereSyncro(),
                source.getCreatedAt());
    }
}
