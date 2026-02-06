package sn.ssi.veille.web.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ssi.veille.web.dto.requests.FavorisRequest;
import sn.ssi.veille.web.dto.requests.UpdateFavorisRequest;
import sn.ssi.veille.web.dto.responses.FavorisResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

@RequestMapping("/api/v1/favoris")
public interface FavorisController {

    @GetMapping
    ResponseEntity<PageResponse<FavorisResponse>> getMyFavoris(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    @GetMapping("/{id}")
    ResponseEntity<FavorisResponse> getFavorisById(
        @PathVariable String id
    );

    @GetMapping("/check/{articleId}")
    ResponseEntity<Boolean> isArticleInFavoris(
        @PathVariable String articleId
    );

    @GetMapping("/count")
    ResponseEntity<Long> getFavorisCount();

    @GetMapping("/search")
    ResponseEntity<PageResponse<FavorisResponse>> searchFavorisByTag(
        @RequestParam String tag,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );

    @PostMapping
    ResponseEntity<FavorisResponse> addToFavoris(
        @Valid @RequestBody FavorisRequest request
    );

    @PutMapping("/{id}")
    ResponseEntity<FavorisResponse> updateFavoris(
        @PathVariable String id,
        @Valid @RequestBody UpdateFavorisRequest request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> removeFavoris(
        @PathVariable String id
    );

    @DeleteMapping("/article/{articleId}")
    ResponseEntity<Void> removeFavorisByArticle(
        @PathVariable String articleId
    );
}
