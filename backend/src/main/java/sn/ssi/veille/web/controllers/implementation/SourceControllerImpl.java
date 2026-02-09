package sn.ssi.veille.web.controllers.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sn.ssi.veille.services.implementation.SourceServiceImpl;
import sn.ssi.veille.services.ScrapingService;
import sn.ssi.veille.web.controllers.SourceController;
import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

/**
 * Implémentation du contrôleur Source.
 * Expose les endpoints REST pour la gestion des sources de veille.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@RestController
public class SourceControllerImpl implements SourceController {

    private final SourceServiceImpl sourceService;
    private final ScrapingService scrapingService;

    public SourceControllerImpl(SourceServiceImpl sourceService, ScrapingService scrapingService) {
        this.sourceService = sourceService;
        this.scrapingService = scrapingService;
    }

    @Override
    public ResponseEntity<List<SourceResponse>> getAllSources() {
        return ResponseEntity.ok(sourceService.getAllSources());
    }

    @Override
    public ResponseEntity<List<SourceResponse>> getActiveSources() {
        return ResponseEntity.ok(sourceService.getActiveSources());
    }

    @Override
    public ResponseEntity<SourceResponse> getSourceById(String id) {
        return ResponseEntity.ok(sourceService.getSourceById(id));
    }

    @Override
    public ResponseEntity<SourceResponse> createSource(SourceRequest request) {
        SourceResponse created = sourceService.createSource(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<SourceResponse> updateSource(String id, SourceRequest request) {
        return ResponseEntity.ok(sourceService.updateSource(id, request));
    }

    @Override
    public ResponseEntity<SourceResponse> activateSource(String id) {
        return ResponseEntity.ok(sourceService.activateSource(id));
    }

    @Override
    public ResponseEntity<SourceResponse> deactivateSource(String id) {
        return ResponseEntity.ok(sourceService.deactivateSource(id));
    }

    @Override
    public ResponseEntity<Void> deleteSource(String id) {
        sourceService.deleteSource(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MessageResponse> triggerScraping(String id) {
        // Lance le scraping synchrone pour la source donnée
        int count = scrapingService.scrapeSource(id).size();
        return ResponseEntity
                .ok(MessageResponse.success("Scraping terminé. " + count + " nouveaux articles collectés."));
    }
}
