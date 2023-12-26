package com.gmail.apachdima.ptt.execution.mapper;

import com.gmail.apachdima.ptt.common.dto.simulation.SimulationResponseDTO;
import com.gmail.apachdima.ptt.execution.model.Simulation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SimulationMapper {

    SimulationResponseDTO toSimulationResponseDTO(Simulation simulation);
}
