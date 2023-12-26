package com.gmail.apachdima.ptt.execution.helper.impl;

import com.gmail.apachdima.ptt.common.constant.common.CommonConstant;
import com.gmail.apachdima.ptt.common.constant.message.Error;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequest;
import com.gmail.apachdima.ptt.common.exception.PTTApplicationException;
import com.gmail.apachdima.ptt.execution.constant.TestExecutionConstant;
import com.gmail.apachdima.ptt.execution.helper.CommandPreparationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class CommandPreparationHelperImpl implements CommandPreparationHelper {

    private final MessageSource messageSource;

    @Override
    public String prepareCommand(TestExecutionRequest request) {
        //http://computer-database.gatling.io
        return TestExecutionConstant.BASE_COMMAND.getValue()
            .concat(CommonConstant.SPACE.getValue())
            .concat(TestExecutionConstant.CommandParameter.BASE_URL.getValue())
            .concat(CommonConstant.EQUAL.getValue())
            .concat(request.baseUrl());
    }

    @Override
    public Process runCommand(String command, Locale locale) {
        Path workDirectory = Paths.get(
            System.getProperty(TestExecutionConstant.WORK_DIRECTORY.getValue()),
            TestExecutionConstant.TEST_EXECUTION_DIRECTORY.getValue());

        boolean isWindows = System.getProperty(TestExecutionConstant.OS_NAME.getValue())
            .toLowerCase()
            .startsWith(TestExecutionConstant.WINDOWS.getValue());

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File(workDirectory.toString()));
        builder.redirectErrorStream(true);
        if (isWindows) {
            builder.command(TestExecutionConstant.CMD_COMMAND.getValue(), "/c", command);
        } else {
            builder.command(TestExecutionConstant.SH_COMMAND.getValue(), "-c", command);
        }
        try {
            return builder.start();
        } catch (IOException e) {
            throw new PTTApplicationException(
                messageSource.getMessage(
                    Error.TEST_EXECUTION_START_COMMAND_ERROR.getKey(), new Object[]{e.getMessage()}, locale));
        }
    }
}
