package com.gmail.apachdima.ptt.common.dto.simulation;

import java.time.LocalDateTime;

public record SimulationResponseDTO(
    String simulationId,
    String simulationClass,
    LocalDateTime addedAt
) {
}
