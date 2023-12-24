package com.gmail.apachdima.ptt.common.dto.execution;

import lombok.Builder;

@Builder
public record TestExecutionResponse(
    String executionId
) {
}
