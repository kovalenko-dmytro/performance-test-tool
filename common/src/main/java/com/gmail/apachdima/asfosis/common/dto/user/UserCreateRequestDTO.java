package com.gmail.apachdima.asfosis.common.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
    @NotBlank
    @Size(min = 2, max = 255)
    String userName,
    @NotBlank
    @Size(min = 4, max = 255)
    String password,
    @NotBlank
    @Size(min = 2, max = 50)
    String firstName,
    @NotBlank
    @Size(min = 2, max = 50)
    String lastName,
    @NotBlank
    @Size(max = 255)
    @Email
    String email
) {


}
