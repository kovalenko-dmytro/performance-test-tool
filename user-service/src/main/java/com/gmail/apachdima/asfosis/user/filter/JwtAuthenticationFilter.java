package com.gmail.apachdima.asfosis.user.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.apachdima.asfosis.common.constant.api.OpenApiAsset;
import com.gmail.apachdima.asfosis.common.constant.common.CommonConstant;
import com.gmail.apachdima.asfosis.common.constant.message.Error;
import com.gmail.apachdima.asfosis.common.dto.RestApiErrorResponseDTO;
import com.gmail.apachdima.asfosis.common.exception.AuthenticationException;
import com.gmail.apachdima.asfosis.user.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsServiceImpl;
    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(CommonConstant.AUTH_HEADER.getValue());
        if (Objects.isNull(authHeader) || !authHeader.startsWith(CommonConstant.BEARER_AUTH_HEADER_PREFIX.getValue())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String userName = jwtService.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (Objects.nonNull(userName) && Objects.isNull(authentication)) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            RestApiErrorResponseDTO errorResponse = RestApiErrorResponseDTO.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}
