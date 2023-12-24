package com.gmail.apachdima.ptt.common.dto.execution;

import lombok.Builder;

@Builder
public record TestExecutionRequest(
    String foo,
    String bar
) {
}
