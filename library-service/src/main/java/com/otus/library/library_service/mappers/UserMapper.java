package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.request.UserReqDto;
import com.otus.library.library_service.dto.response.UserRespDto;
import com.otus.library.library_service.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserReqDto request);

    UserRespDto toResponse(User user);
}
