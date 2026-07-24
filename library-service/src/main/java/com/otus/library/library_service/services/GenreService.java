package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.request.GenreReqDto;
import com.otus.library.library_service.dto.response.GenreRespDto;

import java.util.List;

public interface GenreService {

    List<GenreRespDto> findAll();

    GenreRespDto create(GenreReqDto request);

    void deleteById(long id);
}
