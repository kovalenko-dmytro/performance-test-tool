package com.gmail.apachdima.ptt.execution.controller;

import com.gmail.apachdima.ptt.common.constant.common.CommonConstant;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequestDTO;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponseDTO;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponseDTO;
import com.gmail.apachdima.ptt.execution.constant.TestExecutionConstant;
import com.gmail.apachdima.ptt.execution.service.TestExecutionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "Test execution Service REST API")
@RestController
@RequestMapping(value = "/api/v1/simulations/{simulationId}/test-executions")
@RequiredArgsConstructor
public class TestExecutionController {

    private final TestExecutionService testExecutionService;

    @PostMapping()
    public ResponseEntity<?> execute(
        @PathVariable String simulationId,
        @RequestBody @Valid TestExecutionRequestDTO request,
        HttpServletRequest httpRequest,
        Principal principal,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        String executionId = UUID.randomUUID().toString();
        String currentUrl = httpRequest.getRequestURL().toString();
        testExecutionService.execute(simulationId, executionId, request, currentUrl, principal.getName(), locale);
        return ResponseEntity.accepted().location(createProgressUri(executionId, currentUrl)).build();
    }

    @GetMapping(value = "/{executionId}/progress")
    public ResponseEntity<?> progress(
        @PathVariable String simulationId,
        @PathVariable String executionId
    ) {
        TestExecutionStatusResponseDTO status = testExecutionService.getLatestTestExecutionStatus(executionId);
        return Objects.isNull(status)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok().body(status);
    }

    @GetMapping(value = "/{executionId}")
    public ResponseEntity<TestExecutionResponseDTO> getExecution(
        @PathVariable String simulationId,
        @PathVariable String executionId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok().body(testExecutionService.getExecution(executionId, locale));
    }

    private static URI createProgressUri(String executionId, String currentUrl) {
        return URI.create(currentUrl
            .concat(CommonConstant.SLASH.getValue()
                .concat(executionId)
                .concat(TestExecutionConstant.PROGRESS_API_URI_PATH.getValue())));
    }
}
