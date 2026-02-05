package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.User;
import sn.ssi.veille.web.dto.requests.RegisterRequest;
import sn.ssi.veille.web.dto.requests.UpdateUserRequest;
import sn.ssi.veille.web.dto.responses.UserResponse;

/**
 * Mapper MapStruct pour la conversion entre User entity et DTOs.
 * 
 * <p>Les juniors doivent utiliser ce mapper pour toutes les conversions User.</p>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Convertit un User entity vers un UserResponse DTO.
     *
     * @param user l'entité User
     * @return le DTO UserResponse
     */
    UserResponse toResponse(User user);

    /**
     * Convertit une requête d'inscription vers une entité User.
     * Note: Le mot de passe doit être hashé séparément dans le service.
     *
     * @param request la requête d'inscription
     * @return l'entité User (sans mot de passe hashé)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);

    /**
     * Met à jour une entité User existante avec les données de la requête.
     *
     * @param request la requête de mise à jour
     * @param user l'entité User à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}
