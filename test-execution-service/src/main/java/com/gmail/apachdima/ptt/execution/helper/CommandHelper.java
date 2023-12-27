package com.gmail.apachdima.ptt.execution.helper;

import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequestDTO;

import java.util.Locale;

public interface CommandHelper {

    String prepareCommand(String simulationClass, TestExecutionRequestDTO request);
    Process runCommand(String command, Locale locale);
}
