package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.request.CommentReqDto;
import com.otus.library.library_service.dto.response.CommentRespDto;
import com.otus.library.library_service.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentReqDto request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CommentRespDto toResponse(Comment comment);
}
