package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.request.AuthorReqDto;
import com.otus.library.library_service.dto.response.AuthorRespDto;

import java.util.List;

public interface AuthorService {

    List<AuthorRespDto> findAll();

    AuthorRespDto create(AuthorReqDto request);

    void deleteById(long id);
}
