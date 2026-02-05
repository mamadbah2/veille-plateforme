package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

import java.util.List;

/**
 * Mapper MapStruct pour la conversion entre Categorie entity et DTOs.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategorieMapper {

    /**
     * Convertit une Categorie entity vers un CategorieResponse DTO.
     *
     * @param categorie l'entité Categorie
     * @return le DTO CategorieResponse
     */
    CategorieResponse toResponse(Categorie categorie);

    /**
     * Convertit une liste de Categorie entities vers une liste de CategorieResponse DTOs.
     *
     * @param categories la liste des entités Categorie
     * @return la liste des DTOs CategorieResponse
     */
    List<CategorieResponse> toResponseList(List<Categorie> categories);

    /**
     * Convertit une requête de création vers une entité Categorie.
     *
     * @param request la requête de création
     * @return l'entité Categorie
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Categorie toEntity(CategorieRequest request);

    /**
     * Met à jour une entité Categorie existante avec les données de la requête.
     *
     * @param request la requête de mise à jour
     * @param categorie l'entité Categorie à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CategorieRequest request, @MappingTarget Categorie categorie);
}
