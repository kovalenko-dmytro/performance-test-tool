package com.gmail.apachdima.ptt.common.dto.simulation;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ShortSimulationResponseDTO(
    String simulationId,
    String simulationClass,
    LocalDateTime addedAt
) {
}
