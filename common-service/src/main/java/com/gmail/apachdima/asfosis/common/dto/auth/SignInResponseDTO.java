package com.gmail.apachdima.asfosis.common.dto.auth;

import lombok.Builder;

@Builder
public record SignInResponseDTO (
    String tokenType,
    String accessToken,
    long accessTokenExpired
) {}
