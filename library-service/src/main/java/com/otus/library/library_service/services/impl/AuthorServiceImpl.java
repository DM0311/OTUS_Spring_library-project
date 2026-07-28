package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.request.AuthorReqDto;
import com.otus.library.library_service.dto.response.AuthorRespDto;
import com.otus.library.library_service.mappers.AuthorMapper;
import com.otus.library.library_service.model.entity.Author;
import com.otus.library.library_service.repositories.AuthorRepository;
import com.otus.library.library_service.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    private final AuthorMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorRespDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuthorRespDto create(AuthorReqDto request) {
        Author saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
