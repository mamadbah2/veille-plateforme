package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

public interface SourceMapper {
    SourceResponse toResponse(Source source);
    
    List<SourceResponse> toResponseList(List<Source> sources);
    
    Source toEntity(SourceRequest request);

    void updateEntity(SourceRequest request, @MappingTarget Source source);
}
