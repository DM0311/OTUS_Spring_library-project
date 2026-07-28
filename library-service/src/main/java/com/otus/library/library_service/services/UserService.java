package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.request.UserReqDto;
import com.otus.library.library_service.dto.response.UserRespDto;
import com.otus.library.library_service.model.enums.Role;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserRespDto> findAll();

    UserRespDto getById(long id);

    UserRespDto create(UserReqDto request);

    void deleteById(long id);

    void changePassword(Long id, String currentPassword, String newPassword);

    UserRespDto updateProfile(Long id, String email, String fullName);

    UserRespDto assignRoles(Long userId, Set<Role> roles);

    UserRespDto block(Long userId, boolean flag);
}
