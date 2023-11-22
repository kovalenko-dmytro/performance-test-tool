package com.gmail.apachdima.asfosis.common.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignInRequestDTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String userName;
    @Size(min = 2, max = 50)
    private String password;
}
