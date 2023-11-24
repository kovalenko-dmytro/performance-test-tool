package com.gmail.apachdima.asfosis.user.service.impl;


import com.gmail.apachdima.asfosis.common.constant.common.CommonConstant;
import com.gmail.apachdima.asfosis.common.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UserResponseDTO;
import com.gmail.apachdima.asfosis.user.mapper.UserMapper;
import com.gmail.apachdima.asfosis.user.model.User;
import com.gmail.apachdima.asfosis.user.service.AuthService;
import com.gmail.apachdima.asfosis.user.service.JWTService;
import com.gmail.apachdima.asfosis.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserMapper userMapper;

    @Override
    public SignInResponseDTO signIn(SignInRequestDTO request, Locale locale) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(request.userName(), request.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User authenticatedUser = userService.getByUsername(authentication.getName(), locale);
        String accessToken = jwtService.generateAccessToken(authenticatedUser);
        return SignInResponseDTO.builder()
            .tokenType(CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue().trim())
            .accessToken(accessToken)
            .accessTokenExpired(jwtService.getAccessTokenExpirationTime())
            .build();
    }

    @Override
    public UserResponseDTO getCurrentUser(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getByUsername(authentication.getName(), locale);
        return userMapper.toUserResponseDTO(currentUser);
    }

    @Override
    public void signOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            session.invalidate();
        }

        final String authHeader = request.getHeader(CommonConstant.AUTH_HEADER.getValue());
        final String token = authHeader.substring(7);
        jwtService.invalidateToken(token);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
    }
}
