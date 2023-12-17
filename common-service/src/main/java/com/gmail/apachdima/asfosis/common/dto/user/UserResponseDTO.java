package com.gmail.apachdima.asfosis.common.dto.user;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserResponseDTO(
    String username,
    String firstName,
    String lastName,
    String email,
    boolean enabled,
    LocalDateTime created,
    Set<String> roles
) {}
