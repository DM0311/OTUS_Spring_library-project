package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.request.CommentReqDto;
import com.otus.library.library_service.dto.response.CommentRespDto;

import java.util.List;

public interface CommentService {

    CommentRespDto createComment(CommentReqDto request);

    CommentRespDto updateComment(long id, CommentReqDto request);

    void deleteComment(long id);

    List<CommentRespDto> getCommentsByBook(long id);

    List<CommentRespDto> getCommentsByUser(long id);
}
