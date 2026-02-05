package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.Favoris;
import sn.ssi.veille.web.dto.requests.FavorisRequest;
import sn.ssi.veille.web.dto.requests.UpdateFavorisRequest;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;
import sn.ssi.veille.web.dto.responses.FavorisResponse;

import java.util.List;

/**
 * Mapper MapStruct pour la conversion entre Favoris entity et DTOs.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FavorisMapper {

    /**
     * Convertit un Favoris entity vers un FavorisResponse DTO.
     * 
     * @param favoris l'entité Favoris
     * @param article le DTO de l'article associé
     * @return le DTO FavorisResponse
     */
    // @Mapping(target = "article", source = "article")
    // FavorisResponse toResponse(Favoris favoris, ArticleSummaryResponse article);

    /**
     * Convertit une requête de création vers une entité Favoris.
     *
     * @param request la requête de création
     * @param userId l'ID de l'utilisateur
     * @return l'entité Favoris
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "createdAt", ignore = true)
    Favoris toEntity(FavorisRequest request, String userId);

    /**
     * Met à jour une entité Favoris existante avec les données de la requête.
     *
     * @param request la requête de mise à jour
     * @param favoris l'entité Favoris à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "articleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(UpdateFavorisRequest request, @MappingTarget Favoris favoris);
}
