package com.gmail.apachdima.asfosis.common.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCreateRequestDTO {

    @NotBlank
    @Size(min = 2, max = 255)
    private String userName;
    @NotBlank
    @Size(min = 4, max = 255)
    private String password;
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;
    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

}
