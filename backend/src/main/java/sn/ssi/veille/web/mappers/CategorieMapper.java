package sn.ssi.veille.web.mappers;

import org.mapstruct.MappingTarget;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;

public interface CategorieMapper {

    CategorieResponse toResponse(Categorie categorie);

    List<CategorieResponse> toResponseList(List<Categorie> categories);

    Categorie toEntity(CategorieRequest request);

    void updateEntity(CategorieRequest request, @MappingTarget Categorie categorie);
}
