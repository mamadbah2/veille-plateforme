package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;

public interface CategorieService {

    CategorieResponse createCategorie(CategorieRequest request);

    CategorieResponse getCategorieById(String id);

    CategorieResponse getCategorieByNom(String nom);

    List<CategorieResponse> getAllCategories();

    CategorieResponse updateCategorie(String id, CategorieRequest request);

    void deleteCategorie(String id);
}
