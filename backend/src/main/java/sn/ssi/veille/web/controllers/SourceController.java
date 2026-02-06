package sn.ssi.veille.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.MessageResponse;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

@RequestMapping("/api/v1/sources")
public interface SourceController {

    /**
     * Récupère la liste de toutes les sources configurées, qu'elles soient actives ou non.
     * 
     * @return La liste complète des sources.
     */
    @GetMapping
    ResponseEntity<List<SourceResponse>> getAllSources();

    /**
     * Récupère uniquement les sources actuellement actives et prêtes à être scrapées.
     * Cette méthode est utile pour voir quelles sources sont prises en compte par le moteur de veille.
     * 
     * @return La liste des sources actives.
     */
    @GetMapping("/active")
    ResponseEntity<List<SourceResponse>> getActiveSources();

    /**
     * Récupère les détails d'une source spécifique par son identifiant.
     * 
     * @param id L'identifiant de la source.
     * @return Les détails de la source demandée.
     */
    @GetMapping("/{id}")
    ResponseEntity<SourceResponse> getSourceById(
        @PathVariable String id
    );

    // ==================== ENDPOINTS ADMIN ====================

    /**
     * Crée et configure une nouvelle source d'information.
     * 
     * @param request Les données de la source à créer (URL, nom, type, etc.).
     * @return La source nouvellement créée.
     */
    @PostMapping
    ResponseEntity<SourceResponse> createSource(
        @Valid @RequestBody SourceRequest request
    );

    /**
     * Met à jour les informations d'une source existante.
     * 
     * @param id L'identifiant de la source à modifier.
     * @param request Les nouvelles données de la source.
     * @return La source mise à jour.
     */
    @PutMapping("/{id}")
    ResponseEntity<SourceResponse> updateSource(
        @PathVariable String id,
        @Valid @RequestBody SourceRequest request
    );

    /**
     * Active une source spécifique. Elle sera incluse lors des prochains cycles de scraping.
     * 
     * @param id L'identifiant de la source à activer.
     * @return La source activée.
     */
    @PatchMapping("/{id}/activate")
    ResponseEntity<SourceResponse> activateSource(
        @PathVariable String id
    );

    /**
     * Désactive une source spécifique (soft delete ou simple désactivation).
     * Elle ne sera plus interrogée par le moteur de scraping.
     * 
     * @param id L'identifiant de la source à désactiver.
     * @return La source désactivée.
     */
    @PatchMapping("/{id}/deactivate")
    ResponseEntity<SourceResponse> deactivateSource(
        @PathVariable String id
    );

    /**
     * Supprime définitivement une source du système.
     * 
     * @param id L'identifiant de la source à supprimer.
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSource(
        @PathVariable String id
    );

    /**
     * Déclenche manuellement et immédiatement le scraping pour une source donnée.
     * Utile pour tester une configuration ou forcer une mise à jour.
     * 
     * @param id L'identifiant de la source à scraper.
     * @return Un message confirmant le lancement de l'opération.
     */
    @PostMapping("/{id}/scrape")
    ResponseEntity<MessageResponse> triggerScraping(
        @PathVariable String id
    );
}