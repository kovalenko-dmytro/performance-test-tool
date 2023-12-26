package com.gmail.apachdima.ptt.execution.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TestExecutionConstant {

    BASE_COMMAND("mvn io.gatling:gatling-maven-plugin:test"),
    WORK_DIRECTORY("user.dir"),
    TEST_EXECUTION_DIRECTORY("test-execution-directory"),
    OS_NAME("os.name"),
    WINDOWS("windows"),
    CMD_COMMAND("cmd.exe"),
    SH_COMMAND("sh"),
    LOG_FILE_CONTENT_TYPE("text/plain"),
    LOG_FILE_NAME_PREFIX("test-execution-log-"),
    LOG_FILE_EXTENSION(".txt"),
    PROGRESS_API_URI_PATH("/progress");

    private final String value;

    @RequiredArgsConstructor
    @Getter
    public enum CommandParameter {

        BASE_URL("-DbaseUrl"),
        SIMULATION_CLASS("-DsimulationClass");

        private final String value;
    }
}
