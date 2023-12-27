package com.gmail.apachdima.ptt.execution.service;

import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequestDTO;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponseDTO;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponseDTO;

import java.util.Locale;

public interface TestExecutionService {
    void execute(String simulationId, String executionId, TestExecutionRequestDTO request,
                 String currentUrl, String executedBy, Locale locale);
    TestExecutionStatusResponseDTO getLatestTestExecutionStatus(String executionId);
    TestExecutionResponseDTO getExecution(String executionId, Locale locale);
}
