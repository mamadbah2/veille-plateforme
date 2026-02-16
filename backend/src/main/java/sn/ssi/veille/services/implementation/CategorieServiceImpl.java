package sn.ssi.veille.services.implementation;

import org.springframework.stereotype.Service;
import sn.ssi.veille.exceptions.CategorieNotFoundException;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.services.CategorieService;
import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;

/**
 * Implémentation du service Categorie.
 * Gère les opérations CRUD pour les catégories d'articles.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Service
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;
    private final sn.ssi.veille.models.repositories.StoryRepository storyRepository;

    public CategorieServiceImpl(CategorieRepository categorieRepository,
            sn.ssi.veille.models.repositories.StoryRepository storyRepository) {
        this.categorieRepository = categorieRepository;
        this.storyRepository = storyRepository;
    }

    @Override
    public CategorieResponse createCategorie(CategorieRequest request) {
        Categorie categorie = Categorie.builder()
                .nomCategorie(request.nomCategorie())
                .description(request.description())
                .couleur(request.couleur())
                .icone(request.icone())
                // .imageUrl() handled separately or null
                .build();

        Categorie saved = categorieRepository.save(categorie);
        return toResponse(saved);
    }

    @Override
    public CategorieResponse getCategorieById(String id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new CategorieNotFoundException("Catégorie non trouvée avec l'id: " + id));
        return toResponse(categorie);
    }

    @Override
    public CategorieResponse getCategorieByNom(String nom) {
        Categorie categorie = categorieRepository.findByNomCategorie(nom)
                .orElseThrow(() -> new CategorieNotFoundException("Catégorie non trouvée avec le nom: " + nom));
        return toResponse(categorie);
    }

    @Override
    public List<CategorieResponse> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategorieResponse updateCategorie(String id, CategorieRequest request) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new CategorieNotFoundException("Catégorie non trouvée avec l'id: " + id));

        if (request.nomCategorie() != null)
            categorie.setNomCategorie(request.nomCategorie());
        if (request.description() != null)
            categorie.setDescription(request.description());
        if (request.couleur() != null)
            categorie.setCouleur(request.couleur());
        if (request.icone() != null)
            categorie.setIcone(request.icone());
        // imageUrl update logic missing in request DTO for now, assumed handled by
        // other means or default

        Categorie updated = categorieRepository.save(categorie);
        return toResponse(updated);
    }

    @Override
    public void deleteCategorie(String id) {
        if (!categorieRepository.existsById(id)) {
            throw new CategorieNotFoundException("Catégorie non trouvée avec l'id: " + id);
        }
        categorieRepository.deleteById(id);
    }

    private CategorieResponse toResponse(Categorie categorie) {
        long count = 0;
        if (categorie.getNomCategorie() != null) {
            count = storyRepository.countByCategoriesContaining(categorie.getNomCategorie());
        }

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
