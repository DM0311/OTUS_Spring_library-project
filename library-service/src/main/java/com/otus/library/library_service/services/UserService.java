package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.request.UserReqDto;
import com.otus.library.library_service.dto.response.UserRespDto;

import java.util.List;

public interface UserService {

    List<UserRespDto> findAll();

    UserRespDto getById(long id);

    UserRespDto create(UserReqDto request);

    void deleteById(long id);
}
