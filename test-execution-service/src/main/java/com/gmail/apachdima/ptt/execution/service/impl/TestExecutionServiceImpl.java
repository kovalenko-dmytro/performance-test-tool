package com.gmail.apachdima.ptt.execution.service.impl;

import com.gmail.apachdima.ptt.common.constant.common.CommonConstant;
import com.gmail.apachdima.ptt.common.constant.execution.TestExecutionStatus;
import com.gmail.apachdima.ptt.common.constant.message.Error;
import com.gmail.apachdima.ptt.common.constant.model.Model;
import com.gmail.apachdima.ptt.common.constant.mq.MessageBrokerConstant;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequest;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponse;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponse;
import com.gmail.apachdima.ptt.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.ptt.common.exception.EntityNotFoundException;
import com.gmail.apachdima.ptt.common.exception.PTTApplicationException;
import com.gmail.apachdima.ptt.execution.context.MessageBrokerContext;
import com.gmail.apachdima.ptt.execution.context.TestExecutionContext;
import com.gmail.apachdima.ptt.execution.helper.CommandPreparationHelper;
import com.gmail.apachdima.ptt.execution.helper.LogFileCreationHelper;
import com.gmail.apachdima.ptt.execution.helper.MessageBrokerHelper;
import com.gmail.apachdima.ptt.execution.mapper.TestExecutionMapper;
import com.gmail.apachdima.ptt.execution.model.TestExecution;
import com.gmail.apachdima.ptt.execution.repository.TestExecutionRepository;
import com.gmail.apachdima.ptt.execution.service.TestExecutionService;
import com.gmail.apachdima.ptt.file.storage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TestExecutionServiceImpl implements TestExecutionService {

    private final MessageBrokerHelper messageBrokerHelper;
    private final CommandPreparationHelper commandPreparationHelper;
    private final LogFileCreationHelper logFileCreationHelper;
    private final FileStorageService fileStorageService;
    private final TestExecutionRepository testExecutionRepository;
    private final TestExecutionMapper testExecutionMapper;
    private final MessageSource messageSource;

    @Async
    @Override
    public void execute(String executionId, TestExecutionRequest request, String currentUrl, String executedBy, Locale locale) {
        MessageBrokerContext mbc = messageBrokerHelper.init(executionId);
        TestExecutionContext tec =
            new TestExecutionContext(executionId,0.0f, TestExecutionStatus.SUBMITTED,null, mbc);
        messageBrokerHelper.send(tec);

        try {
            LocalDateTime startedAt = LocalDateTime.now();
            String command = commandPreparationHelper.prepareCommand(request);
            tec.setStatus(TestExecutionStatus.STARTED);
            messageBrokerHelper.send(tec);

            Process process = commandPreparationHelper.runCommand(command, locale);
            tec.setStatus(TestExecutionStatus.RUNNING);
            messageBrokerHelper.send(tec);

            MultipartFile log = logFileCreationHelper.createLog(process, tec, messageBrokerHelper, locale);

            List<FileResponseDTO> logs = fileStorageService.upload(new MultipartFile[]{log}, locale);
            if (Objects.isNull(logs) || logs.isEmpty()) {
                tec.setProgress(100.0f);
                tec.setStatus(TestExecutionStatus.TERMINATED);
                messageBrokerHelper.send(tec);
                throw new PTTApplicationException(
                    messageSource.getMessage(
                        Error.TEST_EXECUTION_PROCESS_LOG_CREATION_ERROR.getKey(), null, locale));
            }

            testExecutionRepository.save(TestExecution.builder()
                    .executionId(executionId)
                    .executedBy(executedBy)
                    .startedAt(startedAt)
                    .finishedAt(LocalDateTime.now())
                    .status(TestExecutionStatus.FINISHED)
                    .log(fileStorageService.getStoredFileModel(logs.get(0).fileId(), locale))
                .build());

            tec.setProgress(100.0f);
            tec.setResultUrl(currentUrl.concat(CommonConstant.SLASH.getValue() + executionId));
            tec.setStatus(TestExecutionStatus.FINISHED);
            messageBrokerHelper.send(tec);
        } catch (Exception e) {
            tec.setProgress(100.0f);
            tec.setStatus(TestExecutionStatus.TERMINATED);
            messageBrokerHelper.send(tec);
            throw new PTTApplicationException(
                messageSource.getMessage(
                    Error.TEST_EXECUTION_PROCESS_TERMINATION_ERROR.getKey(), new Object[]{e.getMessage()}, locale));
        }
    }

    @Override
    public TestExecutionStatusResponse getLatestTestExecutionStatus(String executionId) {
        String queueName = MessageBrokerConstant.TEST_EXECUTION_QUEUE_PREFIX.getValue() + executionId;
        return (TestExecutionStatusResponse) messageBrokerHelper.receive(queueName);
    }

    @Override
    public TestExecutionResponse getExecution(String executionId, Locale locale) {
        return testExecutionMapper.toTestExecutionResponseDTO(getById(executionId, locale));
    }

    private TestExecution getById(String executionId, Locale locale) {
        Object[] params = new Object[]{Model.TEST_EXECUTION.getName(), Model.Field.ID.getFieldName(), executionId};
        return testExecutionRepository
            .findById(executionId)
            .orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage(Error.ENTITY_NOT_FOUND.getKey(), params, locale)));
    }
}
