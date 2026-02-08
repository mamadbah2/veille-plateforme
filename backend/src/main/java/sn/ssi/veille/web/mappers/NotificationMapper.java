package sn.ssi.veille.web.mappers;

import sn.ssi.veille.models.entities.Notification;
import sn.ssi.veille.web.dto.responses.NotificationResponse;

import java.util.List;

public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
    
    List<NotificationResponse> toResponseList(List<Notification> notifications);
}
