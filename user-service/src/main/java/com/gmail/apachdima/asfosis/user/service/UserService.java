package com.gmail.apachdima.asfosis.user.service;

import com.gmail.apachdima.asfosis.common.dto.auth.SignUpRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UpdateUserRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UserCreateRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UserResponseDTO;
import com.gmail.apachdima.asfosis.user.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;

public interface UserService {

    List<UserResponseDTO> findAll(Pageable pageable);
    UserResponseDTO findById(String userId, Locale locale);
    User getByUsername(String name, Locale locale);
    UserResponseDTO update(String userId, UpdateUserRequestDTO request, Locale locale);
    UserResponseDTO create(UserCreateRequestDTO request);
    void delete(String userId, Locale locale);
}
