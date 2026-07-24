package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.request.UserReqDto;
import com.otus.library.library_service.dto.response.UserRespDto;
import com.otus.library.library_service.exception.ResourceNotFoundException;
import com.otus.library.library_service.exception.UserAlreadyExistsException;
import com.otus.library.library_service.mappers.UserMapper;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.repositories.UserRepository;
import com.otus.library.library_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserRespDto> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserRespDto getById(long id) {
        return userRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден, id: " + id));
    }

    @Override
    @Transactional
    public UserRespDto create(UserReqDto request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Пользователь с email " + request.email() + " уже существует");
        }

        User saved = userRepository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }
}
