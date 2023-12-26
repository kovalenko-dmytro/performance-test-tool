package com.gmail.apachdima.ptt.execution.service;

import com.gmail.apachdima.ptt.common.dto.simulation.SimulationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Locale;

public interface SimulationService {
    Page<SimulationResponseDTO> getSimulations(Pageable pageable);
    SimulationResponseDTO getSimulation(String simulationId, Locale locale);
}
