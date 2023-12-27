package com.gmail.apachdima.ptt.execution.service.impl;

import com.gmail.apachdima.ptt.common.constant.common.CommonConstant;
import com.gmail.apachdima.ptt.common.constant.execution.TestExecutionStatus;
import com.gmail.apachdima.ptt.common.constant.message.Error;
import com.gmail.apachdima.ptt.common.constant.model.Model;
import com.gmail.apachdima.ptt.common.constant.mq.MessageBrokerConstant;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequestDTO;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponseDTO;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponseDTO;
import com.gmail.apachdima.ptt.common.dto.file.FileResponseDTO;
import com.gmail.apachdima.ptt.common.exception.EntityNotFoundException;
import com.gmail.apachdima.ptt.common.exception.PTTApplicationException;
import com.gmail.apachdima.ptt.execution.context.MessageBrokerContext;
import com.gmail.apachdima.ptt.execution.context.TestExecutionContext;
import com.gmail.apachdima.ptt.execution.helper.CommandHelper;
import com.gmail.apachdima.ptt.execution.helper.LogFileCreationHelper;
import com.gmail.apachdima.ptt.execution.helper.MessageBrokerHelper;
import com.gmail.apachdima.ptt.execution.mapper.TestExecutionMapper;
import com.gmail.apachdima.ptt.execution.model.Simulation;
import com.gmail.apachdima.ptt.execution.model.TestExecution;
import com.gmail.apachdima.ptt.execution.repository.TestExecutionRepository;
import com.gmail.apachdima.ptt.execution.service.SimulationService;
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
    private final CommandHelper commandHelper;
    private final LogFileCreationHelper logFileCreationHelper;
    private final FileStorageService fileStorageService;
    private final SimulationService simulationService;
    private final TestExecutionRepository testExecutionRepository;
    private final TestExecutionMapper testExecutionMapper;
    private final MessageSource messageSource;

    @Async
    @Override
    public void execute(String simulationId, String executionId, TestExecutionRequestDTO request,
                        String currentUrl, String executedBy, Locale locale) {
        Simulation simulation = simulationService.getSimulationModel(simulationId, locale);
        MessageBrokerContext mbc = messageBrokerHelper.init(executionId);
        TestExecutionContext tec =
            new TestExecutionContext(executionId,0.0f, TestExecutionStatus.SUBMITTED,null, mbc);
        messageBrokerHelper.send(tec);

        try {
            LocalDateTime startedAt = LocalDateTime.now();
            String command = commandHelper.prepareCommand(simulation.getSimulationClass(), request);
            tec.setStatus(TestExecutionStatus.STARTED);
            messageBrokerHelper.send(tec);

            Process process = commandHelper.runCommand(command, locale);
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
                    .simulation(simulation)
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
    public TestExecutionStatusResponseDTO getLatestTestExecutionStatus(String executionId) {
        String queueName = MessageBrokerConstant.TEST_EXECUTION_QUEUE_PREFIX.getValue() + executionId;
        return (TestExecutionStatusResponseDTO) messageBrokerHelper.receive(queueName);
    }

    @Override
    public TestExecutionResponseDTO getExecution(String executionId, Locale locale) {
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
