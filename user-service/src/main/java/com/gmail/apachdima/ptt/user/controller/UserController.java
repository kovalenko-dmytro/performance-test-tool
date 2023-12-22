package com.gmail.apachdima.ptt.user.controller;


import com.gmail.apachdima.ptt.common.dto.user.UpdateUserRequestDTO;
import com.gmail.apachdima.ptt.common.dto.user.UserCreateRequestDTO;
import com.gmail.apachdima.ptt.common.dto.user.UserResponseDTO;
import com.gmail.apachdima.ptt.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Tag(name = "User Service REST API")
@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(userService.findAll(pageable));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResponseDTO> findById(
        @PathVariable("userId") String userId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok().body(userService.findById(userId, locale));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping(value = "/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponseDTO> update(
        @PathVariable("userId") String userId,
        @Valid @RequestBody UpdateUserRequestDTO request,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok().body(userService.update(userId, request, locale));
    }

    @DeleteMapping(value = "/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> delete(
        @PathVariable("userId") String userId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        userService.delete(userId, locale);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
