package com.gmail.apachdima.ptt.execution.controller;

import com.gmail.apachdima.ptt.common.dto.simulation.SimulationResponseDTO;
import com.gmail.apachdima.ptt.execution.service.SimulationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Tag(name = "Simulation Service REST API")
@RestController
@RequestMapping(value = "/api/v1/simulations")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @GetMapping()
    public ResponseEntity<Page<SimulationResponseDTO>> getSimulations(Pageable pageable) {
        return ResponseEntity.ok().body(simulationService.getSimulations(pageable));
    }

    @GetMapping(value = "/{simulationId}")
    public ResponseEntity<SimulationResponseDTO> getSimulation(
        @PathVariable String simulationId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok().body(simulationService.getSimulation(simulationId, locale));
    }
}
