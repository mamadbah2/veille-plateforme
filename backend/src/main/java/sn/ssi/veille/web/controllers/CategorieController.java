package sn.ssi.veille.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;

@RequestMapping("/api/v1/categories")
public interface CategorieController {
    @GetMapping
    ResponseEntity<List<CategorieResponse>> getAllCategories();

    @GetMapping("/{id}")
    ResponseEntity<CategorieResponse> getCategorieById(
        @PathVariable String id
    );

    // ==================== ENDPOINTS ADMIN ====================

    @PostMapping
    ResponseEntity<CategorieResponse> createCategorie(
        @Valid @RequestBody CategorieRequest request
    );
    
    @PutMapping("/{id}")
    ResponseEntity<CategorieResponse> updateCategorie(
        @PathVariable String id,
        @Valid @RequestBody CategorieRequest request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCategorie(
        @PathVariable String id
    );
}
