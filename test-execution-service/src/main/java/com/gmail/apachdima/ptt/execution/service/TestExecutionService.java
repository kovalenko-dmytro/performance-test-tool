package com.gmail.apachdima.ptt.execution.service;

import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequest;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponse;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponse;

import java.util.Locale;

public interface TestExecutionService {
    void execute(String executionId, TestExecutionRequest request, String currentUrl, String executedBy, Locale locale);
    TestExecutionStatusResponse getLatestTestExecutionStatus(String executionId);
    TestExecutionResponse getExecution(String executionId, Locale locale);
}
