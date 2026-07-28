package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.request.BookReqDto;
import com.otus.library.library_service.dto.response.BookRespDto;
import com.otus.library.library_service.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "genres", ignore = true)
    Book toEntity(BookReqDto request);

    BookRespDto toResponse(Book book);
}
