package com.gmail.apachdima.ptt.execution.mapper;

import com.gmail.apachdima.ptt.common.dto.execution.ShortTestExecutionResponseDTO;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponseDTO;
import com.gmail.apachdima.ptt.execution.model.TestExecution;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TestExecutionMapper {

    TestExecutionResponseDTO toTestExecutionResponseDTO(TestExecution testExecution);
    ShortTestExecutionResponseDTO toShortTestExecutionResponseDTO(TestExecution testExecution);
}
