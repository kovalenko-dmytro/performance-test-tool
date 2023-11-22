package com.gmail.apachdima.asfosis.common.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SignInResponseDTO {

    private String tokenType;
    private String accessToken;
    private long accessTokenExpired;
}
