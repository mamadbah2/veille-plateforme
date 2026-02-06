package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.Favoris;
import sn.ssi.veille.web.dto.requests.FavorisRequest;
import sn.ssi.veille.web.dto.requests.UpdateFavorisRequest;
/**
 * Mapper MapStruct pour la conversion entre Favoris entity et DTOs.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FavorisMapper {
    // FavorisResponse toResponse(Favoris favoris, ArticleSummaryResponse article);
    
    Favoris toEntity(FavorisRequest request, String userId);
    
    void updateEntity(UpdateFavorisRequest request, @MappingTarget Favoris favoris);
}
