package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.response.NotificationRespDto;
import com.otus.library.library_service.model.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    NotificationRespDto toResponse(Notification notification);
}
