package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.request.GenreReqDto;
import com.otus.library.library_service.dto.response.GenreRespDto;
import com.otus.library.library_service.mappers.GenreMapper;
import com.otus.library.library_service.model.entity.Genre;
import com.otus.library.library_service.repositories.GenreRepository;
import com.otus.library.library_service.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;

    private final GenreMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<GenreRespDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GenreRespDto create(GenreReqDto request) {
        Genre saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
