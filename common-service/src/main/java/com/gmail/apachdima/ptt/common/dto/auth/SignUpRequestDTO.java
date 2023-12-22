package com.gmail.apachdima.ptt.common.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
    @NotBlank
    @Size(min = 2, max = 50)
    String userName,
    @Size(min = 2, max = 50)
    String password,
    @NotBlank
    @Size(min = 2, max = 50)
    String firstName,
    @NotBlank
    @Size(min = 2, max = 50)
    String lastName,
    @NotBlank
    @Email
    String email
) {
}
