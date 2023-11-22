package com.gmail.apachdima.asfosis.user.controller;

import com.gmail.apachdima.asfosis.common.dto.auth.SignInRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.auth.SignInResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.auth.SignUpRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.user.UserResponseDTO;
import com.gmail.apachdima.asfosis.user.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Tag(name = "Authentication REST API")
@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-in")
    public ResponseEntity<SignInResponseDTO> signIn(
        @Valid @RequestBody SignInRequestDTO request,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok().body(authService.signIn(request, locale));
    }

    @GetMapping(value = "/current-user")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok().body(authService.getCurrentUser(locale));
    }

    @GetMapping(value = "/sign-out")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        authService.signOut(request);
        return ResponseEntity.ok().build();
    }
}
