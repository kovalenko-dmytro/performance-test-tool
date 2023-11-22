package com.gmail.apachdima.asfosis.user.service;

import com.gmail.apachdima.asfosis.common.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.auth.SignUpRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;

public interface AuthService {

    SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO, Locale locale);
    UserResponseDTO getCurrentUser(Locale locale);
    void signOut(HttpServletRequest request);
}
