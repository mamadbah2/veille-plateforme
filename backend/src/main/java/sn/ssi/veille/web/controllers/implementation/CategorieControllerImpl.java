package sn.ssi.veille.web.controllers.implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sn.ssi.veille.services.implementation.CategorieServiceImpl;
import sn.ssi.veille.web.controllers.CategorieController;
import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;

/**
 * Implémentation du contrôleur Categorie.
 * Expose les endpoints REST pour la gestion des catégories.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@RestController
public class CategorieControllerImpl implements CategorieController {

    private final CategorieServiceImpl categorieService;

    public CategorieControllerImpl(CategorieServiceImpl categorieService) {
        this.categorieService = categorieService;
    }

    @Override
    public ResponseEntity<List<CategorieResponse>> getAllCategories() {
        return ResponseEntity.ok(categorieService.getAllCategories());
    }

    @Override
    public ResponseEntity<CategorieResponse> getCategorieById(String id) {
        return ResponseEntity.ok(categorieService.getCategorieById(id));
    }

    @Override
    public ResponseEntity<CategorieResponse> createCategorie(CategorieRequest request) {
        CategorieResponse created = categorieService.createCategorie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<CategorieResponse> updateCategorie(String id, CategorieRequest request) {
        return ResponseEntity.ok(categorieService.updateCategorie(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteCategorie(String id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.noContent().build();
    }
}
