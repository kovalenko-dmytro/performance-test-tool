package com.gmail.apachdima.ptt.execution.helper;

import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequest;

import java.util.Locale;

public interface CommandPreparationHelper {

    String prepareCommand(TestExecutionRequest request);
    Process runCommand(String command, Locale locale);
}
