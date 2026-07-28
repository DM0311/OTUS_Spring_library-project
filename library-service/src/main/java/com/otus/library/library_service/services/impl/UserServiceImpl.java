package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.request.UserReqDto;
import com.otus.library.library_service.dto.response.UserRespDto;
import com.otus.library.library_service.exception.PasswordMismatchException;
import com.otus.library.library_service.exception.ResourceNotFoundException;
import com.otus.library.library_service.exception.UserAlreadyExistsException;
import com.otus.library.library_service.mappers.UserMapper;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.Role;
import com.otus.library.library_service.repositories.UserRepository;
import com.otus.library.library_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

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


    @Override
    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден, id: " + id));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordMismatchException("Неверный текущий пароль");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserRespDto updateProfile(Long id, String email, String fullName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден, id: " + id));
        user.setEmail(email);
        user.setFullName(fullName);
        User saved = userRepository.save(user);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserRespDto assignRoles(Long userId, Set<Role> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        user.setRoles(roles);
        User saved = userRepository.save(user);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserRespDto block(Long userId, boolean flag) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        user.setBlocked(flag);
        User saved = userRepository.save(user);
        return mapper.toResponse(saved);
    }
}
