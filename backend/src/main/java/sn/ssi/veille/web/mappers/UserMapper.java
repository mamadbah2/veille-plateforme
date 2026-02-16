package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import sn.ssi.veille.models.entities.User;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

    User toEntity(RegisterRequest request);

    void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}
