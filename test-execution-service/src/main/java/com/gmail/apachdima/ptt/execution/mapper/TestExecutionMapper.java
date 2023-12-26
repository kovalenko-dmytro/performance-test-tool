package com.gmail.apachdima.ptt.execution.mapper;

import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponse;
import com.gmail.apachdima.ptt.execution.model.TestExecution;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TestExecutionMapper {

    TestExecutionResponse toTestExecutionResponseDTO(TestExecution testExecution);
}
