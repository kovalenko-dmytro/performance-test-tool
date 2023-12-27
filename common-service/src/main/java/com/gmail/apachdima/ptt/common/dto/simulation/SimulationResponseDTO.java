package com.gmail.apachdima.ptt.common.dto.simulation;

import com.gmail.apachdima.ptt.common.dto.execution.ShortTestExecutionResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SimulationResponseDTO(
    String simulationId,
    String simulationClass,
    LocalDateTime addedAt,
    List<ShortTestExecutionResponseDTO> testExecutions
) {
}
