package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.request.GenreReqDto;
import com.otus.library.library_service.dto.response.GenreRespDto;
import com.otus.library.library_service.model.entity.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre toEntity(GenreReqDto request);

    GenreRespDto toResponse(Genre genre);
}
