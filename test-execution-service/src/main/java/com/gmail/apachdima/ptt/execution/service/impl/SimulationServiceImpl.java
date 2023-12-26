package com.gmail.apachdima.ptt.execution.service.impl;

import com.gmail.apachdima.ptt.common.constant.message.Error;
import com.gmail.apachdima.ptt.common.constant.model.Model;
import com.gmail.apachdima.ptt.common.dto.simulation.SimulationResponseDTO;
import com.gmail.apachdima.ptt.common.exception.EntityNotFoundException;
import com.gmail.apachdima.ptt.execution.mapper.SimulationMapper;
import com.gmail.apachdima.ptt.execution.model.Simulation;
import com.gmail.apachdima.ptt.execution.repository.SimulationRepository;
import com.gmail.apachdima.ptt.execution.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {

    private final SimulationRepository simulationRepository;
    private final SimulationMapper simulationMapper;
    private final MessageSource messageSource;

    @Override
    public Page<SimulationResponseDTO> getSimulations(Pageable pageable) {
        Page<Simulation> simulations = simulationRepository.findAll(pageable);
        return simulations.isEmpty()
            ? Page.empty()
            : simulations.map(simulationMapper::toSimulationResponseDTO);
    }

    @Override
    public SimulationResponseDTO getSimulation(String simulationId, Locale locale) {
        return simulationMapper.toSimulationResponseDTO(getById(simulationId, locale));
    }

    private Simulation getById(String simulationId, Locale locale) {
        Object[] params = new Object[]{Model.SIMULATION.getName(), Model.Field.ID.getFieldName(), simulationId};
        return simulationRepository
            .findById(simulationId)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(Error.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }
}
