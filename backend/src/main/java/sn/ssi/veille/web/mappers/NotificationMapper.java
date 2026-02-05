package sn.ssi.veille.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.Notification;
import sn.ssi.veille.web.dto.responses.NotificationResponse;

import java.util.List;

/**
 * Mapper MapStruct pour la conversion entre Notification entity et DTOs.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {

    /**
     * Convertit une Notification entity vers un NotificationResponse DTO.
     *
     * @param notification l'entité Notification
     * @return le DTO NotificationResponse
     */
    NotificationResponse toResponse(Notification notification);

    /**
     * Convertit une liste de Notification entities vers une liste de NotificationResponse DTOs.
     *
     * @param notifications la liste des entités Notification
     * @return la liste des DTOs NotificationResponse
     */
    List<NotificationResponse> toResponseList(List<Notification> notifications);
}
