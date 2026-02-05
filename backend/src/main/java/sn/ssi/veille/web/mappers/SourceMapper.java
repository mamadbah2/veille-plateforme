package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

/**
 * Mapper MapStruct pour la conversion entre Source entity et DTOs.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SourceMapper {

    /**
     * Convertit une Source entity vers un SourceResponse DTO.
     *
     * @param source l'entité Source
     * @return le DTO SourceResponse
     */
    SourceResponse toResponse(Source source);

    /**
     * Convertit une liste de Source entities vers une liste de SourceResponse DTOs.
     *
     * @param sources la liste des entités Source
     * @return la liste des DTOs SourceResponse
     */
    List<SourceResponse> toResponseList(List<Source> sources);

    /**
     * Convertit une requête de création vers une entité Source.
     *
     * @param request la requête de création
     * @return l'entité Source
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "derniereSyncro", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Source toEntity(SourceRequest request);

    /**
     * Met à jour une entité Source existante avec les données de la requête.
     *
     * @param request la requête de mise à jour
     * @param source l'entité Source à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "derniereSyncro", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(SourceRequest request, @MappingTarget Source source);
}
