package com.gmail.apachdima.ptt.execution.helper.impl;

import com.gmail.apachdima.ptt.common.constant.common.CommonConstant;
import com.gmail.apachdima.ptt.common.constant.message.Error;
import com.gmail.apachdima.ptt.common.exception.PTTApplicationException;
import com.gmail.apachdima.ptt.execution.constant.TestExecutionConstant;
import com.gmail.apachdima.ptt.execution.context.TestExecutionContext;
import com.gmail.apachdima.ptt.execution.helper.LogFileCreationHelper;
import com.gmail.apachdima.ptt.execution.helper.MessageBrokerHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class LogFileCreationHelperImpl implements LogFileCreationHelper {

    private static final Pattern PROGRESS_PATTERN = Pattern.compile("\\[[#-]+\\]\\s*(\\d+\\.?\\d*)\\%");

    private final MessageSource messageSource;

    @Override
    public MultipartFile createLog(Process process,
                                   TestExecutionContext tec,
                                   MessageBrokerHelper messageBrokerHelper,
                                   Locale locale
    ) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher;
        String line;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = reader.readLine()) != null) {
                builder
                    .append(line)
                    .append(CommonConstant.CARRIAGE_RETURN.getValue());

                matcher = PROGRESS_PATTERN.matcher(line);
                if (matcher.find()) {
                    tec.setProgress(Float.parseFloat(matcher.group(1)));
                    messageBrokerHelper.send(tec);
                }
            }
            process.waitFor();
            byte[] bytes = builder.toString().getBytes(StandardCharsets.UTF_8);
            String fileName = createLogName(tec.getExecutionId());
            return new MockMultipartFile(fileName, fileName, TestExecutionConstant.LOG_FILE_CONTENT_TYPE.getValue(), bytes);
        } catch (IOException e) {
            throw new PTTApplicationException(
                messageSource.getMessage(
                    Error.TEST_EXECUTION_READ_PROCESS_ERROR.getKey(), new Object[]{e.getMessage()}, locale));
        } catch (InterruptedException e) {
            throw new PTTApplicationException(
                messageSource.getMessage(
                    Error.TEST_EXECUTION_PROCESS_INTERRUPTION_ERROR.getKey(), new Object[]{e.getMessage()}, locale));
        }
    }

    private static String createLogName(String executionId) {
        return TestExecutionConstant.LOG_FILE_NAME_PREFIX.getValue()
            .concat(executionId)
            .concat(CommonConstant.DASH.getValue())
            .concat(String.valueOf(LocalDateTime.now()))
            .concat(TestExecutionConstant.LOG_FILE_EXTENSION.getValue());
    }
}
