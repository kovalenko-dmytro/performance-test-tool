package com.gmail.apachdima.asfosis.common.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public record RestApiErrorResponseDTO (
     HttpStatus status,
     String message,
     List<String> errors,
     String timestamp
) {}
