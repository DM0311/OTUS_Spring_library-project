package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.request.AuthorReqDto;
import com.otus.library.library_service.dto.response.AuthorRespDto;
import com.otus.library.library_service.model.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorReqDto request);

    AuthorRespDto toResponse(Author author);
}
