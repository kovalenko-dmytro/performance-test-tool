package com.gmail.apachdima.ptt.execution.helper;

import com.gmail.apachdima.ptt.execution.context.TestExecutionContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

public interface LogFileCreationHelper {

    MultipartFile createLog(
        Process process,
        TestExecutionContext tec,
        MessageBrokerHelper messageBrokerHelper,
        Locale locale);
}
