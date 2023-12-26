package com.gmail.apachdima.ptt.common.dto.execution;

import com.gmail.apachdima.ptt.common.constant.execution.TestExecutionStatus;
import lombok.Builder;

@Builder
public record TestExecutionStatusResponseDTO(
    String executionId,
    float progress,
    TestExecutionStatus status,
    String resultUrl
) {
}
