package com.gmail.apachdima.ptt.common.dto.execution;

import com.gmail.apachdima.ptt.common.constant.execution.TestExecutionStatus;
import com.gmail.apachdima.ptt.common.dto.file.FileResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ShortTestExecutionResponseDTO(
    String executionId,
    String executedBy,
    TestExecutionStatus status,
    LocalDateTime startedAt,
    LocalDateTime finishedAt,
    FileResponseDTO log
) {
}
