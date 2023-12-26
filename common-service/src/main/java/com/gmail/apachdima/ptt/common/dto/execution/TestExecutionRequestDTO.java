package com.gmail.apachdima.ptt.common.dto.execution;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TestExecutionRequestDTO(
    String simulationClass,
    @NotBlank
    String baseUrl
) {
}
