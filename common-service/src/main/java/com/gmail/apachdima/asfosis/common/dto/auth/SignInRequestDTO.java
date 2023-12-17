package com.gmail.apachdima.asfosis.common.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequestDTO (
    @NotBlank
    @Size(min = 2, max = 50)
    String userName,
    @Size(min = 2, max = 50)
    String password
) {


}
