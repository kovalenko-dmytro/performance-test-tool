package com.gmail.apachdima.ptt.common.dto.execution;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TestExecutionRequest(
    String foo,
    String bar,
    @NotBlank
    String baseUrl
) {
}
